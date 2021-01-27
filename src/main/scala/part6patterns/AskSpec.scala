package com.github.argentino.udemy.akka
package part6patterns

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class AskSpec extends TestKit(ActorSystem("AskSpec"))
  with ImplicitSender with WordSpecLike with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import AskSpec._

}

object AskSpec {

  // this code is somewhere else in your app
  case class Read(key: String)
  case class Write(key: String, value: String)
  class KVActor extends Actor with ActorLogging {

    override def receive: Receive = online(Map())

    def online(kv: Map[String, String]): Receive = {
      case Read(key) =>
        log.info(s"Trying to read the value at the key $key")
        sender() ! kv.get(key) // Option[String]
      case Write(key, value) =>
        log.info(s"Writing the value $value for the key $key")
        context.become(online(kv + (key -> value)))
    }
  }

  // user authenticator actor
  case class RegisterUser(username: String, password: String)
  case class Authenticate(username: String, password: String)
  case class AuthFailure(message: String)
  case object AuthSuccess

  class AuthManager extends Actor with ActorLogging {

    private val authDb = context.actorOf(Props[KVActor])

    override def receive: Receive = {
      case RegisterUser(username, password) => authDb ! Write(username, password)
      case Authenticate(username, password) => 
        authDb ! Read(username)
        context.become(waitingForPassword(username, sender()))
    }

    def waitingForPassword(username: String, ref: ActorRef): Receive = {
      case password: Option[String] => // do password checks here
    }
  }
}
