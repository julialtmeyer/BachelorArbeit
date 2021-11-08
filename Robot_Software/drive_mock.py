from math import tan
from queue import Queue
from collections import deque

LOCATION_X = 0
LOCATION_Y = 0
ORIENTATION = 0
MAP_SCALE_FACTOR = 0.19857
COMMANDS = Queue()


def drive(data, x, y, o):
    global LOCATION_X
    global LOCATION_Y
    global ORIENTATION
    global COMMANDS

    LOCATION_X = x
    LOCATION_Y = y
    ORIENTATION = o

    COMMANDS.put(data)

    while not COMMANDS.empty():
        item = COMMANDS.get()
        if "commands" in item.keys():
            for command in item.get("commands"):
                if "TurnCommand" in command.keys():
                    direction = command.get("TurnCommand").get("direction")
                    drive_turn(direction)

                elif "DriveCommand" in command.keys():
                    direction = command.get("DriveCommand").get("direction")
                    drive_distance(direction)
        else:
            if "TurnCommand" in item.keys():
                direction = item.get("TurnCommand").get("direction")
                drive_turn(direction)

            elif "DriveCommand" in item.keys():
                direction = item.get("DriveCommand").get("direction")
                drive_distance(direction)

    location_arr = [LOCATION_X, LOCATION_Y, ORIENTATION]
    return location_arr


def drive_distance(distance):
    update_location("drive", distance)


def drive_turn(angle):
    orientation = 0

    if ORIENTATION == 0 and angle < 0:
        orientation = 360 + angle

    elif ORIENTATION + angle > 360:
        orientation = ORIENTATION + angle - 360

    else:
        orientation = ORIENTATION + angle
    orientation = abs(orientation)
    if orientation == 360:
        orientation = 0
    update_location("turn", orientation)


def update_location(command, value):
    global LOCATION_X
    global LOCATION_Y
    global ORIENTATION

    if command == "drive":
        if ORIENTATION == 0 or ORIENTATION == 180:
            if ORIENTATION == 0:
                # decrement Y
                LOCATION_Y = LOCATION_Y - get_relative_distance(value)
            else:
                # increment Y
                LOCATION_Y = LOCATION_Y + get_relative_distance(value)

        elif ORIENTATION == 90 or ORIENTATION == 270:
            if ORIENTATION == 90:
                # increment X
                LOCATION_X = LOCATION_X + get_relative_distance(value)
            else:
                # decrement X
                LOCATION_X = LOCATION_X - get_relative_distance(value)

        else:
            m = tan(ORIENTATION)
    else:
        ORIENTATION = value


def get_relative_distance(distance):
    rel_dis = distance / MAP_SCALE_FACTOR
    rel_dis = round(rel_dis)
    return rel_dis