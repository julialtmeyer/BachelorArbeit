# -*- coding: utf-8 -*-
import base64
import io
import time

from picamera import PiCamera
from time import sleep

framerate = 60
quality = 100
res = (1280, 720)


def init_camera():
    with PiCamera(framerate=framerate, resolution=res) as camera:
        camera.rotation = 180
        image = io.BytesIO()
        camera.capture(image, 'jpeg', quality=quality, use_video_port=True)
        return image.getvalue()
