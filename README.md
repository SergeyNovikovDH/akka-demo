Akka consumer demo
==========

Sample app consumes messages from `queue1` and adds them to `queue2` with a given speed.
Speed can be controlled via http endpoint.

Install docker/docker-compose and sbt (brew install sbt)
```
boot2docker start
docker-compose up

bundle install

sbt run
```

RabbitMQ GUI on http://docker:6001 and http://docker:6002 (guest/guest)

Push 300k messages to queue1
```
ruby push.rb 300000
```

Change consuming rate dynamically via `/throttle` endpoint:
```
curl -X POST 'http://127.0.0.1:9999/throttle' -d 'rate=200'
```
