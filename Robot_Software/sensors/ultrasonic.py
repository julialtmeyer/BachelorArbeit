import grovepi
import time

# set I2C to use the hardware bus
grovepi.set_bus("RPI_1")

# Connect the Grove Ultrasonic Ranger to digital port D4
# SIG,NC,VCC,GND
ultrasonic_ranger = 6


def get_reading():
    return grovepi.ultrasonicRead(ultrasonic_ranger)


def check_obstacle():
    if get_reading() <= 10:
        return True
    else:
        False
