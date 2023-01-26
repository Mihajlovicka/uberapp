import requests
import json

from locust import HttpUser, task, between, events
from random import randrange


taxi_stops = [
    (45.238548, 19.848225),   # Stajaliste na keju
    (45.243097, 19.836284),   # Stajaliste kod limanske pijace
    (45.256863, 19.844129),   # Stajaliste kod trifkovicevog trga
    (45.255055, 19.810161),   # Stajaliste na telepu
    (45.246540, 19.849282)    # Stajaliste kod velike menze
]

# @events.test_start.add_listener
# def on_test_start(environment, **kwargs):
    # requests.delete('http://localhost:8080/api/ride')
    # requests.delete('http://localhost:8080/api/car')


class QuickstartUser(HttpUser):
    host = 'http://localhost:8080'
    wait_time = between(0.5, 2)

    def on_start(self):
        self.vehicle = self.client.get('/api/car').json()
        if self.vehicle['status'] == 'AVAILABLE':
            self.departure = taxi_stops[randrange(0, len(taxi_stops))]
            self.get_new_coordinates()
        elif self.vehicle['status'] == 'BUSY':
            self.get_existing_coordinates()

    @task
    def update_vehicle_coordinates(self):
        new_status = self.client.get(f"/api/car/status/{self.vehicle['id']}").text
        status_changed = False
        if self.vehicle['status'] != new_status:
            status_changed = True
            self.vehicle['status'] = new_status

        if len(self.coordinates) > 0:
            new_coordinate = self.coordinates.pop(0)
            self.client.put(f"/api/car/{self.vehicle['id']}", json={
                'latitude': new_coordinate[0],
                'longitude': new_coordinate[1]
            })
            if status_changed:
                self.end_ride()
                if new_status == 'AVAILABLE':
                    self.departure = [new_coordinate[1], new_coordinate[0]]
                    self.get_new_coordinates()
                if new_status == 'BUSY':
                    self.get_existing_coordinates()
        elif len(self.coordinates) == 0 and not status_changed:
            self.end_ride()
            self.departure = self.destination
            self.get_new_coordinates()


    def get_new_coordinates(self):
         #krece od random stanice i ide na jednu random izabranu lokaciju
        self.destination = taxi_stops[randrange(0, len(taxi_stops))]
        while (self.departure[0] == self.destination[0]):
            self.destination = taxi_stops[randrange(0, len(taxi_stops))]

        response = requests.get(f'https://routing.openstreetmap.de/routed-car/route/v1/driving/'
                                f'{self.departure[1]},{self.departure[0]};{self.destination[1]},{self.destination[0]}'
                                f'?geometries=geojson&overview=false&alternatives=true&steps=true')
        self.routeGeoJSON = response.json()
        self.coordinates = [] #dobio je rutu od stanice do lokacije

        for step in self.routeGeoJSON['routes'][0]['legs'][0]['steps']:
            self.coordinates = [*self.coordinates, *step['geometry']['coordinates']] #uzima sve koordinate

        self.ride = self.client.post('/api/ride', json={
            'routeJSON': json.dumps(self.routeGeoJSON),
            'rideStatus': 0,
            'vehicle': {
                'id': self.vehicle['id'],
                'licensePlateNumber': self.vehicle['licensePlateNumber'],
                'latitude': self.coordinates[0][0],
                'longitude': self.coordinates[0][1]
            } # napravi voznju jednu ima ruta   i vozilo     i poctna stanica je postavljena
        }).json()
        self.real_ride = False
        self.fake_ride = True


#da li treba da dodje prvo do pocetne stanice
    def get_existing_coordinates(self):
        self.ride = self.client.get(f"/api/ride/{self.vehicle['id']}").json()
        routeJson = json.loads(self.ride['routeJSON'])
        self.coordinates = routeJson['features'][0]['geometry']['coordinates'] #uzima sve koordinate
        self.departure = [self.coordinates[0][1], self.coordinates[0][0]] #krece od random stanice i ide na jednu random izabranu lokaciju
        self.destination = [self.coordinates[-1][1], self.coordinates[-1][0]]
        self.real_ride = True
        self.fake_ride = False

    def end_ride(self):
        if self.real_ride:
            self.client.put(f"/api/ride/{self.ride['id']}")
        elif self.fake_ride:
            self.client.delete(f"/api/ride/{self.ride['id']}")
