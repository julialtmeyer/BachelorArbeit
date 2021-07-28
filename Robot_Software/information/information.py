import base64
import io
import threading
from time import sleep

from Robot_Software import mqtt_client
from Robot_Software.drivers import brickpi3
from Robot_Software.sensors import camera
from threading import Thread

from picamera import PiCamera

framerate = 60
quality = 100
res = (1280, 720)


brickpi = brickpi3.BrickPi3

class InformationThread(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)

    def publish_battery():
        bat = brickpi.get_voltage_battery()
        bat_str = "\"battery\":\"" + bat + "\""
        return bat_str


    def publish_picture():
        with PiCamera(framerate=framerate, resolution=res) as c:
            image = io.BytesIO()
            c.capture(image, 'jpeg', quality=quality, use_video_port=True)
            img_str = "\"picture\":" + "data:image/jpeg;base64," + base64.b64encode(image.getvalue()).decode()
            return img_str


    def publish_information():
        info_str = "{\"InformationMessage\":{" + publish_battery() + "," + publish_picture() + "," + "}}"
        mqtt_client.publish_on_channel(mqtt_client.ROBOT_INFORMATION, info_str)

    def run(self):
        while True:
            # publish_picture()
            if mqtt_client.MQTT_CLIENT.is_connected():
                print(1)
            sleep(5)
