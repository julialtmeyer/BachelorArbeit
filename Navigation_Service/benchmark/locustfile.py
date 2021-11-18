from locust import HttpUser, task, between

class NavigationTester(HttpUser):
    wait_time = between(1, 5)

    @task
    def navigation(self):
        self.client.post("/navigation/", json={"start_x": "10", "start_y": "444", "dest_x": "1100", "dest_y": "52"})

    def on_start(self):
        self.client.post("/navigation/", json={"start_x": "10", "start_y": "444", "dest_x": "1100", "dest_y": "52"})
