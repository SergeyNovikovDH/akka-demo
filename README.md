Akka consumer demo
==========

Sample app consumes messages from `queue1` and adds them to `queue2` with a given speed.
Speed can be dynamically controlled via http endpoint.

**Queue 1**: Input (300k messages):

<img width="605" alt="screenshot 2015-08-20 18 55 30" src="https://cloud.githubusercontent.com/assets/5654118/9393523/b14508aa-4782-11e5-9da0-f21c5cf14c58.png">

**Queue 2**: Output (speed: 100 msgs/s, then 200, 500, and 100):

<img width="612" alt="screenshot 2015-08-20 18 55 39" src="https://cloud.githubusercontent.com/assets/5654118/9393525/b2f69f60-4782-11e5-96b7-73eeea405331.png">


Install docker/docker-compose and sbt (brew install sbt). Run the app:
```
boot2docker start
docker-compose up

bundle install

sbt run
```

RabbitMQ GUI: http://docker:6001 and http://docker:6002 (login/pass: guest/guest)

Push 300k messages to queue1:
```
ruby push.rb 300000
```

Change processing rate:
```
curl -X POST 'http://127.0.0.1:9999/throttle' -d 'rate=200'
```
