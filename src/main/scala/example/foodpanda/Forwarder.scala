package example.foodpanda

import akka.actor._
import akka.contrib.throttle.TimerBasedThrottler
import akka.contrib.throttle.Throttler._
import akka.camel._
import scala.concurrent.duration._

object Forwarder {
  val numberOfWorkers = 10
  val inputQueue = "rabbitmq://docker:5001/test?queue=testqueue&durable=false&autoDelete=false&autoAck=false"
  val outputQueue = "rabbitmq://docker:5002/test?queue=testqueue&durable=false&autoDelete=false&autoAck=false"

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("throttler")
    
    val queueProducer = system.actorOf(Props(classOf[QueueProducer]))
    val throttler = system.actorOf(Props(classOf[TimerBasedThrottler], 100 msgsPer 1.second))
    throttler ! SetTarget(Some(queueProducer))
    val throttleController = system.actorOf(Props(classOf[ThrottleController], throttler))
    val consumer = system.actorOf(Props(classOf[QueueConsumer], throttler))
  }

  class ThrottleController(throttler: ActorRef) extends Consumer {
    def endpointUri = "jetty:http://0.0.0.0:9999/throttle"
    def setThrottle(rate: Int) = throttler ! SetRate(rate msgsPer 1.second)
    
    def receive = {
      case msg: CamelMessage =>
        if (msg.body != null) {
          val body = msg.bodyAs[String]
          val size = """rate=(\d+)""".r.findFirstMatchIn(body) match {
            case Some(ma) =>
              val rate = ma.group(1).toInt
              throttler ! SetRate(rate msgsPer 1.second)
              sender ! s"Changed rate to $rate msgs per second"
            case None =>
              sender ! "should be rate=123"
          }
        }
    }
  }

  class QueueConsumer(producer: ActorRef) extends Consumer {
    def endpointUri = inputQueue 
    override def autoAck = false

    def receive = {
      case Ack =>
        sender ! Ack
      case msg: CamelMessage =>
        producer forward msg
    }
  }

  class QueueProducer extends Actor with Producer {
    def endpointUri = outputQueue

    override def routeResponse(msg: Any): Unit = {
      sender forward Ack
    }
  }
}
