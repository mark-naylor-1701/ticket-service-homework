// author: Mark W. Naylor
// file:   Driver.scala
// date:   2016-Aug-26

import scala.collection.JavaConverters._

import java.util.{ArrayList}
import java.util.Optional

import com.walmart.homework._

object Data {
  val venue = Venue.defaultVenue
  val testVenue = Venue.testingVenue

  val none = Optional.empty()
  val noneInt: Optional[java.lang.Integer] = Optional.empty()

  val email = "mark.naylor.1701@gmail.com"

  def someInt(i: java.lang.Integer): Optional[java.lang.Integer] = Optional.of(i)
}

object Driver extends App {
  import Data._

  println("Hello, Walmart testing world.")

  test1

  // -- Support functions
  def showSeats(venue: Venue) = println("Available seats: " + venue.numSeatsAvailable(noneInt))

  // -- Test funtions

  def test1() {
    showSeats(testVenue)

    println("\n Any seats: " + testVenue.findAndHoldSeats(15, noneInt, noneInt, email))

    showSeats(testVenue)

    println("\nTry best level: " + testVenue.findAndHoldSeats(4, noneInt, someInt(1), email))

    showSeats(testVenue)

    println("\nHow about cheap seats? " + testVenue.findAndHoldSeats(5, someInt(4), noneInt, email))

    showSeats(testVenue)

    val cheapHold =  testVenue.findAndHoldSeats(5, someInt(4), noneInt, email)

    println("\nHow about cheap seats? " + cheapHold)

    showSeats(testVenue)

    println("\nHow about cheap seats? " + testVenue.findAndHoldSeats(5, someInt(4), noneInt, email))

    showSeats(testVenue)

    println("Release hold.")

    if (cheapHold.isPresent())
      testVenue.releaseHold(cheapHold.get())

    println

    showSeats(testVenue)
    
  } // test1()

}
