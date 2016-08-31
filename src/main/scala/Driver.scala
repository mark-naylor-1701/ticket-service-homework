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


  printLabel("testSeatHoldExceed")
  testSeatHoldExceed


  printLabel("testSeatHoldMeetCapacity")
  testSeatHoldMeetCapacity


  printLabel("testSeatHoldMeetCapacityHardRelease")
  testSeatHoldMeetCapacityHardRelease

  printLabel("testSeatHolds")
  testSeatHolds

  //exit(0)

  // -- Test functions

  // ----------------------------------------
  // Seat Hold tests

  def testSeatHoldExceed() {
    var tv: Option[Venue] = None

    try {
      val jtv = Venue.testingVenue
      val seatRequest = 15
      tv = Some(jtv)
      val capacity = jtv.numSeatsAvailable(noneInt)

      val seatHold = jtv.findAndHoldSeats(seatRequest, noneInt, someInt(1), email)
      assert(!seatHold.isPresent(), s"seatHold ${seatHold} should be empty.")
    } finally {
      tv.map(_.close)
    }
  } // testSeatHoldExceed()


  def testSeatHoldMeetCapacity() {
    var tv: Option[Venue] = None

    try {
      val jtv = Venue.testingVenue
      val seatRequest = 5
      tv = Some(jtv)
      val capacity = jtv.numSeatsAvailable(noneInt)
      val expected = capacity - seatRequest

      jtv.findAndHoldSeats(seatRequest, noneInt, toJava(Some(1)), email)
      val seatsAvail = jtv.numSeatsAvailable(noneInt)
      assert(seatsAvail == expected, s"${seatsAvail} != ${expected}.\n")
    } finally {
      tv.map(_.close)
    }
  } // testSeatHoldMeetCapacity()


  def testSeatHoldMeetCapacityHardRelease() {
    var tv: Option[Venue] = None

    try {
      val jtv = Venue.testingVenue
      val seatRequest = 5
      tv = Some(jtv)
      val capacity = jtv.numSeatsAvailable(noneInt)
      val expected = capacity - seatRequest

      val seatHold = toScala(jtv.findAndHoldSeats(seatRequest, noneInt, toJava(Some(1)), email))
      var seatsAvail = jtv.numSeatsAvailable(noneInt)
      assert(seatsAvail == expected, s"${seatsAvail} != ${expected}.\n")

      //jtv.releaseHold(seatHold)
      seatHold.map(jtv.releaseHold(_))
      seatsAvail = jtv.numSeatsAvailable(noneInt)
      assert(seatsAvail == capacity, s"${seatsAvail} != ${capacity}.\n")
    } finally {
      tv.map(_.close)
    }
  } // testSeatHoldMeetCapacityHardRelease()




  @deprecated("Break into smaller tests.", "2016-08-31")
  def testSeatHolds() {
    var tv: Venue = null
    var seatRequest = 0

    try {
      tv = Venue.testingVenue
      val initialCapacity = tv.numSeatsAvailable(noneInt)
      var currentCap = initialCapacity
      var intRes = 0
      var holdCount = 0

      // Hold #1
      // exceed section 1 capacity
      // seatRequest = 15
      // val seatHold1 = tv.findAndHoldSeats(seatRequest, noneInt, someInt(1), email)
      // assert(!seatHold1.isPresent(), s"seatHold1 should be empty.\n")

      // Hold #2
      // meet section 1 capacity
      // seatRequest = 5
      // currentCap -= seatRequest
      // holdCount += 1
      // val seatHold2 = tv.findAndHoldSeats(seatRequest, noneInt, someInt(1), email)
      // assert(seatHold2.isPresent(), s"seatHold2 should not be empty.\n")
      // intRes = tv.numSeatsAvailable(noneInt)
      // assert(intRes == currentCap, s"avalable seats ${intRes} != ${currentCap}\n")
      // intRes = tv.holdCount()
      // assert(intRes == holdCount, s"holds ${intRes} != ${holdCount}\n")

      // // release the hold
      // currentCap += seatRequest
      // holdCount -= 1
      // tv.releaseHold(seatHold2.get)
      // intRes = tv.numSeatsAvailable(noneInt)
      // assert(intRes == currentCap, s"avalable seats ${intRes} != ${currentCap}\n")
      // intRes = tv.holdCount()
      // assert(intRes == holdCount, s"holds ${intRes} != ${holdCount}\n")


      // Hold #3
      // meet section 1 capacity
      seatRequest = 5
      currentCap -= seatRequest
      holdCount += 1
      val seatHold3 = tv.findAndHoldSeats(seatRequest, noneInt, someInt(1), email)
      assert(seatHold3.isPresent(), s"seatHold3 should not be empty.\n")
      intRes = tv.numSeatsAvailable(noneInt)
      assert(intRes == currentCap, s"avalable seats ${intRes} != ${currentCap}\n")
      intRes = tv.holdCount()
      assert(intRes == holdCount, s"holds ${intRes} != ${holdCount}\n")

      // Force pause, venue should release the hold
      Thread.sleep(Defaults.SEATHOLD_LIFESPAN * 4)

      currentCap += seatRequest
      holdCount -= 1
      intRes = tv.numSeatsAvailable(noneInt)
      assert(intRes == currentCap, s"avalable seats ${intRes} != ${currentCap}\n")
      intRes = tv.holdCount()
      assert(intRes == holdCount, s"holds ${intRes} != ${holdCount}\n")


    }
    finally {
      if (tv != null) { tv.close }
    }
  } // testSeatHolds()

  // ----------------------------------------

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
    var tv: Venue = null
    try {
      tv = Venue.testingVenue
      assert(tv != null, s"No Venue returned")

      val initialSeats = tv.numSeatsAvailable(noneInt)
      assert(initialSeats == 40, s"initial seats ${initialSeats} != 40")

      val holdCount = tv.holdCount()
      assert(holdCount == 0, s"seat holds ${holdCount} != ${0}")
    }
    finally {
      if (tv != null) { tv.close }
    }
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

  def toScala[T](t: Optional[T]): Option[T] = {
    if (t.isPresent) Some(t.get) else None
  } // toScala

  def toJava[T](t: Option[T]): Optional[T] = {
    //Optional[T].of(t.getOrElse( {return Optional.empty()} ))
    Optional.of(t.getOrElse( {return Optional.empty()} ))
  } // toJava

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
