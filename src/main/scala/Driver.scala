// author: Mark W. Naylor
// file:   Driver.scala
// date:   2016-Aug-26

import scala.collection.JavaConverters._

import java.util.{ArrayList, Collection, Date, Optional}

import com.walmart.homework._

object Driver extends App {
  import Data._
  import Functions.{printLabel, someInt}

  println("Hello, Walmart testing world.")

  printLabel("testCreateLevel")
  testCreateLevel

  printLabel("testCreateSeat")
  testCreateSeat

  printLabel("testCreateSeatHold")
  testCreateSeatHold

  printLabel("testCreateVenue")
  testCreateVenue

  // -- Test functions

  def testCreateSeat() {
    // Seat depends on having a good level.
    testCreateLevel

    val level = new Level(1, new LevelName("Test Name"), new MonetaryAmount(15.50), 4, 10)
    val rowNumber = new RowNumber(1)
    val seatNumber = new SeatNumber(2)

    val seat = new Seat(rowNumber, seatNumber, level)
    assert(seat != null, "No seat created")


    assert(seat.getLevel == level, s"level ${seat.getLevel} != ${level}")
    assert(seat.getRowNumber == rowNumber, s"Field ${seat.getRowNumber} != ${rowNumber}")
    assert(seat.getSeatNumber == seatNumber, s"Field ${seat.getSeatNumber} != ${seatNumber}")
  } // testCreateSeat()

  def testCreateLevel() {
    val name = new LevelName("Test Name")
    val price = new MonetaryAmount(15.50)
    val rows = 4
    val seats = 10
    val id = 1
    val level = new Level(id, name, price, rows, seats)

    assert(level.getName == name, s"Field ${level.getName} != ${name}")
    assert(level.getPrice == price, s"Field ${level.getPrice} != ${price}")
    assert(level.getId == id, s"Field ${level.getId} != ${id}")
    assert(level.numSeatsAvailable == rows * seats, s"Field ${level.numSeatsAvailable} != ${rows * seats}")
  } // testCreateLevel()

  def testCreateVenue() {
    val tv = Venue.testingVenue
    assert(tv != null, s"No Venue returned")

    val initialSeats = tv.numSeatsAvailable(noneInt)
    assert(initialSeats == 40, s"initial seats ${initialSeats} != 40")

    val holdCount = tv.holdCount()
    assert(holdCount == 0, s"seat holds ${holdCount} != ${0}")
  } // testCreateVenue()

  def testCreateSeatHold() {
    val none: Optional[Collection[Seat]] = Optional.empty()
    val sh = new SeatHold(email, none)

    //println(sh.getExpiration)
    val now = new Date
    val expiration = sh.getExpiration
    assert(expiration > now.getTime, s"Field ${expiration} <= ${now}")

    val size = sh.getSeats.size
    assert(size == 0, s"Failure on SeatHold getSeats, ${size} != 0\n")

    val resEmail = sh.getCustomerEmail
    assert(resEmail == email, s"Failure on SeatHold getCustomerEmail, ${resEmail} != ${email}\n")
  } // testCreateSeatHold()

  @deprecated("Being phase out", "2016-08-30 13:28")
  def test1() {
    val testVenue = Venue.testingVenue
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

  // -- Support functions
  def showSeats(venue: Venue) = println("Available seats: " + venue.numSeatsAvailable(noneInt))

} // Driver




object Data {

  val none = Optional.empty()
  val noneInt: Optional[java.lang.Integer] = Optional.empty()

  val email = "mark.naylor.1701@gmail.com"
} // Data()

object Functions {
  def someInt(i: java.lang.Integer): Optional[java.lang.Integer] = Optional.of(i)

  def printLabel(label: String) = println(s"\n${label}")
} // Functions
