# -*- coding: utf-8 -*-
import mqtt_client
import brickpi3

BP = brickpi3.BrickPi3()

try:

    print(BP.get_motor_status(BP.PORT_A))
    BP.reset_all()
    print(BP.get_motor_status(BP.PORT_A))

    mqtt_client.connect_mqtt()

except KeyboardInterrupt:  # except the program gets interrupted by Ctrl+C on the keyboard.
    BP.reset_all()
