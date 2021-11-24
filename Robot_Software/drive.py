# -*- coding: utf-8 -*-
import threading
import time
import json
import brickpi3
from sensors import ultrasonic
from math import radians, tan

TURN_RADIUS_COUNTER_TURN = 14
TURN_RADIUS_SAME_TURN = 17.5
WHEEL_CIRCUMFERENCE = 13.571  # in cm
ONE_DEGREE = 0.0377  # 1° from wheel in cm
SPEED_DPS = 360  # number in degrees per second
COUNT_PER_REV = 360  # number of encoder steps it takes for drive wheel to spin 360°
MAP_SCALE_FACTOR = 0.19857
LOCATION_X = 0
LOCATION_Y = 0
ORIENTATION = 0
OBSTACLE = False
SLIP_OFFSET = 2.8
BP = brickpi3.BrickPi3()


def drive(data, x, y, o):
    global OBSTACLE
    global LOCATION_X
    global LOCATION_Y
    global ORIENTATION

    LOCATION_X = x
    LOCATION_Y = y
    ORIENTATION = o
    OBSTACLE = False

    BP.set_motor_position(BP.PORT_A, 0)

    if "commands" in data.keys():
        for command in data.get("commands"):
            if "TurnCommand" in command.keys():
                direction = command.get("TurnCommand").get("direction")
                drive_turn(direction)

            elif "DriveCommand" in command.keys():
                direction = command.get("DriveCommand").get("direction")
                drive_distance(direction)
    else:
        if "TurnCommand" in data.keys():
            direction = data.get("TurnCommand").get("direction")
            drive_turn(direction)

        elif "DriveCommand" in data.keys():
            direction = data.get("DriveCommand").get("direction")
            drive_distance(direction)


def drive_distance(distance):
    global OBSTACLE
    # sleep_timer = calculate_sleep_time(distance)
    revs = distance_cm_to_revs(distance)
    num_steps = calculate_steps_for_number_of_revolutions(revs)
    BP.set_motor_position(BP.PORT_A, 0)
    time.sleep(0.2)
    BP.offset_motor_encoder(BP.PORT_B, BP.get_motor_encoder(BP.PORT_B))
    BP.offset_motor_encoder(BP.PORT_C, BP.get_motor_encoder(BP.PORT_C))
    BP.set_motor_position(BP.PORT_B, num_steps)
    BP.set_motor_position(BP.PORT_C, num_steps)

    # BP.set_motor_dps(BP.PORT_B, SPEED_DPS)
    # BP.set_motor_dps(BP.PORT_C, SPEED_DPS)
    # time.sleep(sleep_timer)
    # BP.set_motor_dps(BP.PORT_B, 0)
    # BP.set_motor_dps(BP.PORT_C, 0)


def drive_turn(angle):
    global OBSTACLE
    distance = calculate_counter_turn_distance(angle)
    num_revs = distance_cm_to_revs(distance)
    target_count = calculate_steps_for_number_of_revolutions(num_revs)
    sleep = calculate_sleep_time_revs(target_count)

    BP.set_motor_limits(BP.PORT_B, 0, 360)
    BP.set_motor_limits(BP.PORT_C, 0, 360)
    BP.offset_motor_encoder(BP.PORT_B, BP.get_motor_encoder(BP.PORT_B))
    BP.offset_motor_encoder(BP.PORT_C, BP.get_motor_encoder(BP.PORT_C))

    if angle < 0:
        BP.set_motor_position(BP.PORT_A, -40)
        time.sleep(0.2)
        BP.set_motor_position(BP.PORT_C, -target_count)
        BP.set_motor_position(BP.PORT_B, target_count)

    else:
        BP.set_motor_position(BP.PORT_A, 40)
        time.sleep(0.2)
        BP.set_motor_position(BP.PORT_C, target_count)
        BP.set_motor_position(BP.PORT_B, -target_count)


    if OBSTACLE:
        print("collision")

    time.sleep(sleep)
    BP.set_motor_position(BP.PORT_A, 0)


def calculate_sleep_time(distance):
    speed_cms = SPEED_DPS * ONE_DEGREE
    timer = distance / speed_cms
    return timer


def calculate_sleep_time_revs(revolutions):
    timer = revolutions / 360.0
    return timer


def calculate_counter_turn_distance(angle):
    distance = radians(abs(angle)) * TURN_RADIUS_COUNTER_TURN
    distance = distance * SLIP_OFFSET
    return distance


def calculate_steps_for_number_of_revolutions(num_revs):
    num_steps = num_revs * COUNT_PER_REV
    return num_steps


def distance_cm_to_revs(distance):
    num_revs = distance / WHEEL_CIRCUMFERENCE
    return num_revs


def sub_task(port, num_revs, command, angle):
    global OBSTACLE
    while BP.get_motor_encoder(port) != num_revs:
        update_location(command, angle)
        if ultrasonic.check_obstacle():
            stop_movement()
            OBSTACLE = True
            return True

    return False


def update_location(command, angle):
    global LOCATION_X
    global LOCATION_Y
    global ORIENTATION

    if command == "drive":
        if ORIENTATION == 0 or ORIENTATION == 180:
            if ORIENTATION == 0:
                # decrement Y
                LOCATION_Y = LOCATION_Y - 1
            else:
                # increment Y
                LOCATION_Y = LOCATION_Y + 1

        elif ORIENTATION == 90 or ORIENTATION == 270:
            if ORIENTATION == 90:
                # increment X
                LOCATION_X = LOCATION_X + 1
            else:
                # decrement X
                LOCATION_X = LOCATION_X - 1

        else:
            m = tan(ORIENTATION)
    else:
        ORIENTATION = angle


def stop_movement():
    BP.set_motor_position(BP.PORT_A, 0)
    BP.set_motor_position(BP.PORT_B, BP.get_motor_encoder(BP.PORT_B))
    BP.set_motor_position(BP.PORT_C, BP.get_motor_encoder(BP.PORT_C))