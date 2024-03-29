# -*- coding: utf-8 -*-
import json
import random
import ssl
import logging
import string
import threading

import drive
import information
from paho.mqtt import client as mqtt_client
from pathlib import Path
from sensors import ultrasonic

LOG = logging.getLogger("MQTT")

MAC = ""

dir_path = Path(__file__).parent.absolute().parent.absolute()
CA_CERTIFICATE_PATH = dir_path / "Robot_Software/certificates/ca_certificate.pem"
CLIENT_CERTIFICATE_PATH = dir_path / "Robot_Software/certificates/client_certificate.pem"
CLIENT_KEY_PATH = dir_path / "Robot_Software/certificates/client_key.pem"

MQTT_CLIENT = mqtt_client.Client
ROBOT_REGISTRATION_TOPIC = "data/BrickPi/registration"
ROBOT_INFORMATION_TOPIC = ""
ROBOT_DRIVE_TOPIC = ""

MQTT_SERVER_LOCAL = "192.168.178.49" #when running rabbitmq local, use the local address
MQTT_PORT_LOCAL = 32598
MQTT_SERVER_HTW = "134.96.216.207"
MQTT_PORT_HTW = 8883
USERNAME_HTW = "user"
PASSWORD_HTW = "DgBe3RZVvZ"
USE_CERTIFICATE = False #true when connecting to the DSL RabbitMQ Broker, False when connecting to a locally hosted one

HANDSHAKE = ""
ROBOT_ID = ""
ROBOT_NAME = ""
LOCATION_X = 0
LOCATION_Y = 0
ORIENTATION = 0
LOCAL = False


def connect_mqtt(local):
    global MQTT_CLIENT
    global LOCAL

    LOCAL = local

    MQTT_CLIENT = mqtt_client.Client()

    MQTT_CLIENT.on_connect = on_connect
    MQTT_CLIENT.on_message = on_message
    if USE_CERTIFICATE:
        MQTT_CLIENT.username_pw_set(USERNAME_HTW, PASSWORD_HTW)
        MQTT_CLIENT.tls_set(CA_CERTIFICATE_PATH, CLIENT_CERTIFICATE_PATH, CLIENT_KEY_PATH, cert_reqs=ssl.CERT_NONE,
                            tls_version=ssl.PROTOCOL_TLSv1_2, ciphers=None)
        MQTT_CLIENT.connect(MQTT_SERVER_HTW, MQTT_PORT_HTW, 60)
        LOG.info('MQTT connected to the htw mqtt server')
    else:
        MQTT_CLIENT.connect(MQTT_SERVER_LOCAL, MQTT_PORT_LOCAL, 60)
        LOG.info('MQTT connected to the local mqtt server')

    information_thread()
    publish_heartbeat()
    check_obstacle()

    MQTT_CLIENT.loop_forever()


def on_connect(client, userdata, flags, rc):
    client.subscribe(ROBOT_REGISTRATION_TOPIC)
    print("connected")
    register_robot()
    LOG.info(client, "MQTT connected with result code " + str(rc))


def on_publish(client, userdata, mid):
    LOG.debug(client, "on_publish, mid{}".format(mid))


def on_message(client, userdata, message):
    global ROBOT_NAME
    global ROBOT_ID
    global ROBOT_DRIVE_TOPIC
    global ROBOT_INFORMATION_TOPIC
    global LOCATION_X
    global LOCATION_Y
    global ORIENTATION

    data = json.loads(message.payload.decode("utf-8"))

    if message.topic == ROBOT_REGISTRATION_TOPIC and "response" in data.keys():
        response = data['response']
        if response['hsc'] == HANDSHAKE and response['macAdr'] == MAC:
            print("Registration response received")
            ROBOT_ID = response['id']
            ROBOT_NAME = response['roboterName']
            LOCATION_X = response['robot_info']['location_x']
            LOCATION_Y = response['robot_info']['location_y']
            ORIENTATION = response['robot_info']['direction']
            ROBOT_DRIVE_TOPIC = "data/BrickPi/" + ROBOT_NAME + "/drive"
            print("connected to topic" + ROBOT_DRIVE_TOPIC)
            client.subscribe(ROBOT_DRIVE_TOPIC)
            ROBOT_INFORMATION_TOPIC = "data/BrickPi/" + ROBOT_NAME + "/information"
            print("connected to topic" + ROBOT_INFORMATION_TOPIC)
            client.subscribe(ROBOT_INFORMATION_TOPIC)
    elif message.topic == ROBOT_DRIVE_TOPIC:
        if not LOCAL:
            import drive
            drive.drive(data, LOCATION_X, LOCATION_Y, ORIENTATION)
        else:
            import drive_mock
            location_arr = drive_mock.drive(data, LOCATION_X, LOCATION_Y, ORIENTATION)
            LOCATION_X = location_arr[0]
            LOCATION_Y = location_arr[1]
            ORIENTATION = location_arr[2]


def register_robot():
    global HANDSHAKE
    global MAC

    letters = string.ascii_lowercase
    HANDSHAKE = ''.join(random.choice(letters) for i in range(20))
    MAC = get_mac('wlan0')

    reg_req = "{\"request\": {\"hsc\": \"" + HANDSHAKE + "\",\"macAdr\": \"" + MAC + "\"}}"

    publish_on_topic(ROBOT_REGISTRATION_TOPIC, reg_req)
    print("Registration request send")


def publish_on_topic(topic, payload):
    MQTT_CLIENT.publish(topic, payload)


def publish_on_information(payload):
    print(payload)
    MQTT_CLIENT.publish(ROBOT_INFORMATION_TOPIC, payload)


def get_mac(interface='wlan0'):
    # Return the MAC address of the specified interface
    try:
        str = open('/sys/class/net/%s/address' % interface).read()
    except:
        str = "00:00:00:00:00:00"
    return str[0:17]


def publish_heartbeat():
    heartbeat = "{\"heartbeat\":{" \
                "\"Id\":\"" + str(ROBOT_ID) + "\"," \
                                              "\"robotName\":\"" + str(ROBOT_NAME) + "\"," \
                                                                                     "\"hsc\":\"" + str(
        HANDSHAKE) + "\"," \
                     "\"macAdr\":\"" + str(MAC) + "\"}}"

    if ROBOT_NAME != "":
        print(heartbeat)
        publish_on_topic(ROBOT_REGISTRATION_TOPIC, heartbeat)
    threading.Timer(5, publish_heartbeat).start()


def information_thread():
    if ROBOT_INFORMATION_TOPIC != "":
        information.publish_information(local=LOCAL, topic=ROBOT_INFORMATION_TOPIC, x=LOCATION_X, y=LOCATION_Y,
                                        o=ORIENTATION)
    threading.Timer(0.1, information_thread).start()


def check_obstacle():
    if ultrasonic.check_obstacle():
        drive.stop_movement()

    threading.Timer(0.1, check_obstacle).start()


# for creating random mac adresses when testing without real robot
def rand_mac():
    return "%02x:%02x:%02x:%02x:%02x:%02x" % (
        random.randint(0, 255),
        random.randint(0, 255),
        random.randint(0, 255),
        random.randint(0, 255),
        random.randint(0, 255),
        random.randint(0, 255)
    )
