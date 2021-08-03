# -*- coding: utf-8 -*-
import time
import json
import brickpi3

TURN_RADIUS_COUNTER_TURN = 11.5
TURN_RADIUS_SAME_TURN = 17.5
WHEEL_CIRCUMFERENCE = 13.571  # in cm
ONE_DEGREE = 0.0377  # 1° from wheel in cm
SPEED_DPS = 360  # number in degrees per second
COUNT_PER_REV = 360  # number of encoder steps it takes for drive wheel to spin 360°
MAP_SCALE_FACTOR = 0.19857
LOCATION_X = 0
LOCATION_Y = 0
# pi * radius:
PIVOT_CIRCLE_COUNTER_TURN = 42.411
PIVOT_CIRCLE_SAME_TURN = 54.98
BP = brickpi3.BrickPi3()


def drive(data, x, y):
    global LOCATION_X
    global LOCATION_Y

    LOCATION_X = x
    LOCATION_Y = y

    BP.set_motor_position(BP.PORT_A, 0)

    if "commands" in data.keys():
        for command in data.get("commands"):
            if "TurnCommand" in command.keys():
                direction = command.get("TurnCommand").get("direction")
                drive_corner_counter_turn(direction)

            elif "DriveCommand" in command.keys():
                direction = command.get("DriveCommand").get("direction")
                drive_distance(direction)
    else:
        if "TurnCommand" in data.keys():
            direction = data.get("TurnCommand").get("direction")
            drive_corner_counter_turn(direction)

        elif "DriveCommand" in data.keys():
            direction = data.get("DriveCommand").get("direction")
            drive_distance(direction)

def drive_distance(distance):
    # sleep_timer = calculate_sleep_time(distance)
    num_revs = calculate_revs_for_distance(distance)
    BP.set_motor_position(BP.PORT_A, 0)
    time.sleep(0.2)
    BP.offset_motor_encoder(BP.PORT_B, BP.get_motor_encoder(BP.PORT_B))
    BP.offset_motor_encoder(BP.PORT_C, BP.get_motor_encoder(BP.PORT_C))
    BP.set_motor_position(BP.PORT_B, num_revs)
    BP.set_motor_position(BP.PORT_C, num_revs)
    # BP.set_motor_dps(BP.PORT_B, SPEED_DPS)
    # BP.set_motor_dps(BP.PORT_C, SPEED_DPS)
    # time.sleep(sleep_timer)
    # BP.set_motor_dps(BP.PORT_B, 0)
    # BP.set_motor_dps(BP.PORT_C, 0)


def drive_corner_counter_turn(angle):
    distance = calculate_counter_turn_distance(angle)
    num_revs = calculate_revs_for_distance(distance)
    sleep = calculate_sleep_time_revs(num_revs)

    BP.set_motor_limits(BP.PORT_B, 0, 360)
    BP.set_motor_limits(BP.PORT_C, 0, 360)
    BP.offset_motor_encoder(BP.PORT_B, BP.get_motor_encoder(BP.PORT_B))
    BP.offset_motor_encoder(BP.PORT_C, BP.get_motor_encoder(BP.PORT_C))

    if angle < 0:
        BP.set_motor_position(BP.PORT_A, -40)
        time.sleep(0.2)
        BP.set_motor_position(BP.PORT_C, -num_revs)
        BP.set_motor_position(BP.PORT_B, num_revs)
    else:
        BP.set_motor_position(BP.PORT_A, 40)
        time.sleep(0.2)
        BP.set_motor_position(BP.PORT_C, num_revs)
        BP.set_motor_position(BP.PORT_B, -num_revs)

    time.sleep(sleep)
    BP.set_motor_position(BP.PORT_A, 0)


def calculate_sleep_time(distance):
    speed_cms = SPEED_DPS * ONE_DEGREE
    timer = distance / speed_cms
    return timer


def calculate_sleep_time_revs(revolutions):
    timer = revolutions / 360
    return timer


def calculate_counter_turn_distance(angle):
    distance = abs(angle) / 360 * PIVOT_CIRCLE_COUNTER_TURN
    return distance


def calculate_revs_for_distance(distance):
    num_revs = distance * COUNT_PER_REV
    return num_revs
