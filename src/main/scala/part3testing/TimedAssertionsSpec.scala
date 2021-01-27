package com.github.argentino.udemy.akka
package part3testing

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class TimedAssertionsSpec extends TestKit(ActorSystem("TimedAssertionsSpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    super.afterAll()
    TestKit.shutdownActorSystem(system)
  }

  import TimedAssertionsSpec._

}

object TimedAssertionsSpec {
  // testing scenario
}