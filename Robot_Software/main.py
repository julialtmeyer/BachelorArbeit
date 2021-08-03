# -*- coding: utf-8 -*-
import mqtt_client

local = True

if local:
    import brickpi3
    BP = brickpi3.BrickPi3()

    try:

        print(BP.get_motor_status(BP.PORT_A))
        BP.reset_all()
        print(BP.get_motor_status(BP.PORT_A))

        mqtt_client.connect_mqtt()

    except KeyboardInterrupt:  # except the program gets interrupted by Ctrl+C on the keyboard.
        BP.reset_all()

else:
    mqtt_client.connect_mqtt()
