package com.github.argentino.udemy.akka
package part2actors

import akka.actor.{Actor, ActorRef, Props}

object ChildActors extends App {

  // Actors can create other actors

  object Parent {
    case class CreateChild(name: String)
    case class TellChild(message: String)
  }
  class Parent extends Actor {
    import Parent._

    var child: ActorRef = null
    override def receive: Receive = {
      case CreateChild(name) =>
        println(s"${self.path} creating child")
        // create a new actor right HERE
        val childRef = context.actorOf(Props[Child], name)
        child = childRef
      case TellChild(message) =>
        if(child != null) child forward message
    }
  }

  class Child extends Actor {
    override def receive: Receive = {
      case message => println(s"${self.path} I got: $message")
    }
  }

}
