package com.github.argentino.udemy.akka
package part5infra

import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class FSMSpec extends TestKit(ActorSystem("FSMSpec"))
  with ImplicitSender with WordSpecLike with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import FSMSpec._
}

object FSMSpec {

  /*
    Vending machine
   */

  case class Initialize(inventory: Map[String, Int], prices: Map[String, Int])
  case class RequestProduct(product: String)

  case class Instruction(instruction: String) // message the VM will show on its "screen"
  case class ReceiveMoney(amount: Int)
  case class Deliver(product: String)
  case class GiveBackChange(amount: Int)

  case class VendingError(reason: String)
  case object ReceiveMoneyTimeout

  class VendingMachine extends Actor with ActorLogging {
    override def receive: Receive = ???
  }
}
