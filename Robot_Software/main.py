# -*- coding: utf-8 -*-
import mqtt_client
import brickpi3

BP = brickpi3.BrickPi3()

BP.offset_motor_encoder(BP.PORT_A, BP.get_motor_encoder(BP.PORT_A))

print(BP.get_motor_status(BP.PORT_A))

mqtt_client.connect_mqtt()
