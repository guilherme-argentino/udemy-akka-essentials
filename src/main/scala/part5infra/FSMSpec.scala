package com.github.argentino.udemy.akka
package part5infra

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class FSMSpec extends TestKit(ActorSystem("FSMSpec"))
  with ImplicitSender with WordSpecLike with BeforeAndAfterAll with OneInstancePerTest {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import FSMSpec._
}

object FSMSpec {

}
