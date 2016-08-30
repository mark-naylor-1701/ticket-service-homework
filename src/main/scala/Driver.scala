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


  // List(venue, testVenue) map { venue =>
  //   {
  //     println("Level 1 seats: "  + venue.numSeatsAvailable(someInt(1)))
  //     println("All seats:     "  + venue.numSeatsAvailable(noneInt))
  //   }}

  // List(venue, testVenue).map(venue => println("Level 5 seats: "  + venue.numSeatsAvailable(Optional.of(new Integer(5)))))

  def showSeats = println("Available seats: " + testVenue.numSeatsAvailable(noneInt))

  showSeats

  println("\n Any seats: " + testVenue.findAndHoldSeats(15, noneInt, noneInt, email))

  showSeats

  println("\nTry best level: " + testVenue.findAndHoldSeats(4, noneInt, someInt(1), email))

  showSeats

  println("\nHow about cheap seats? " + testVenue.findAndHoldSeats(5, someInt(4), noneInt, email))

  showSeats

  val cheapHold =  testVenue.findAndHoldSeats(5, someInt(4), noneInt, email)

  println("\nHow about cheap seats? " + cheapHold)

  showSeats

  println("\nHow about cheap seats? " + testVenue.findAndHoldSeats(5, someInt(4), noneInt, email))

  showSeats

  println("Release hold.")

  if (cheapHold.isPresent())
    testVenue.releaseHold(cheapHold.get())

  println

  showSeats

}
