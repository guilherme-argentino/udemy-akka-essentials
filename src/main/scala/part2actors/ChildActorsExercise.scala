package com.github.argentino.udemy.akka
package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.TestActor.NullMessage.sender

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

object ChildActorsExercise extends App {

  // Distributed Word counting

  object WordCounterMaster {

    case class Initialize(nChildren: Int)

    case class WordCountTask(/* TODO */ text: String)

    case class WordCountReply(text:String, count: Int)

  }

  class WordCounterMaster extends Actor {

    import WordCounterMaster._

    var childrenActors: List[ActorRef] = null

    override def receive: Receive = {
      case Initialize(nChildren) =>
        childrenActors = List.tabulate(nChildren)(n => context.actorOf(Props[WordCounterWorker], s"worker-${n}"))
      case message: WordCountTask =>
        childrenActors.head ! message
        childrenActors = circular(childrenActors, 1)
      case message: WordCountReply =>
        println(s"[${self.path}] size of '${message.text}' is ${message.count} - processed by [${sender().path}]")
        sender() ! message // TODO
    }

    @tailrec
    private def circular[A](L: List[A], times: Int ): List[A] = {

      if ( times == 0 || L.size < 2 ) L
      else circular(L.drop(1) :+ L.head , times-1)
    }

  }

  class WordCounterWorker extends Actor {

    import WordCounterMaster._

    override def receive: Receive = {
      case WordCountTask(message: String) =>
        println(s"[${self.path}] received $message")
        sender() ! WordCountReply(message, message.split(" ").length)
    }
  }

  /*
    create WordCounterMaster
    send Initialize(10) to wordCounterMaster
    send "Akka is awesome" to wordCounterMaster
      wcm will send a WordCountTask("...") to one of its children
        child replies with a WordCountReply(3) to the master
      master replies with 3 to the sender.

    requester -> wcm -> wcw
           r  <- wcm <-

   */
  // round robin logic
  // 1,2,3,4,5 and 7 tasks
  // 1,2,3,4,5,1,2

  val system = ActorSystem("ChildActorsExercise")
  val wordCounterMaster = system.actorOf(Props[WordCounterMaster], "master-demo")

  import WordCounterMaster._

  wordCounterMaster ! Initialize(5)
  (1 to 1000).foreach(n => wordCounterMaster ! WordCountTask("Akka is awesome!"))

}
