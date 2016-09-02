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

  printLabel("testLevelCreate")
  testLevelCreate

  printLabel("testSeatCreate")
  testSeatCreate

  printLabel("testSeatHoldCreate")
  testSeatHoldCreate

  printLabel("testSeatHoldRelease")
  testSeatHoldRelease

  printLabel("testVenueCreate")
  testVenueCreate


  printLabel("testSeatHoldExceed")
  testSeatHoldExceed


  printLabel("testSeatHoldMeetCapacity")
  testSeatHoldMeetCapacity


  printLabel("testSeatHoldMeetCapacityExplicitRelease")


  // *TODO* enable test
  // commented out to speed test
  // printLabel("testSeatHoldMeetCapacityTimeOutRelease")
  // testSeatHoldMeetCapacityTimeOutRelease

  

  printLabel("testSeatReserve")
  testSeatReserve


  // end main()

  // -- Test functions

  // ----------------------------------------
  // Seat Hold tests

  def testSeatReserve() {
    var tv: Option[Venue] = None

    try {
      val jtv = Venue.testingVenue
      val seatRequest = 5
      tv = Some(jtv)
      val capacity = jtv.numSeatsAvailable(noneInt)
      val expected = capacity - seatRequest

      val hold = toScala(jtv.findAndHoldSeats(seatRequest, noneInt, toJava(Some(1)), email))
      var seatsAvail = jtv.numSeatsAvailable(noneInt)
      assert(seatsAvail == expected, s"${seatsAvail} != ${expected}.\n")


      hold map {
        hold => {
          val id = hold.getId
          val address = hold.getCustomerEmail
          println (
            jtv.reserveSeats(id, address)
          )
        }
      }

      seatsAvail = jtv.numSeatsAvailable(noneInt)
      assert(seatsAvail == expected, s"${seatsAvail} != ${expected}.\n")

    } finally {
      tv.map(_.close)
    }
  } // testSeatReserve()


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

  def testSeatHoldMeetCapacityExplicitRelease() {
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

      seatHold.map(jtv.releaseHold(_))
      seatsAvail = jtv.numSeatsAvailable(noneInt)
      assert(seatsAvail == capacity, s"${seatsAvail} != ${capacity}.\n")
    } finally {
      tv.map(_.close)
    }
  } // testSeatHoldMeetCapacityExplicitRelease()

  def testSeatHoldMeetCapacityTimeOutRelease() {
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

      Thread.sleep(Defaults.SEATHOLD_LIFESPAN * 2)
      seatsAvail = jtv.numSeatsAvailable(noneInt)
      assert(seatsAvail == capacity, s"${seatsAvail} != ${capacity}.\n")
    } finally {
      tv.map(_.close)
    }
  } // testSeatHoldMeetCapacityTimeOutRelease()

  // ----------------------------------------

  def testSeatCreate() {
    // Seat depends on having a good level.
    testLevelCreate

    val level = new Level(1, new LevelName("Test Name"), new MonetaryAmount(15.50), 4, 10)
    val rowNumber = new RowNumber(1)
    val seatNumber = new SeatNumber(2)

    val seat = new Seat(rowNumber, seatNumber, level)
    assert(seat != null, "No seat created")


    assert(seat.getLevel == level, s"level ${seat.getLevel} != ${level}")
    assert(seat.getRowNumber == rowNumber, s"Field ${seat.getRowNumber} != ${rowNumber}")
    assert(seat.getSeatNumber == seatNumber, s"Field ${seat.getSeatNumber} != ${seatNumber}")
  } // testSeatCreate()

  def testLevelCreate() {
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
  } // testLevelCreate()

  def testVenueCreate() {
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
  } // testVenueCreate()

  def testSeatHoldCreate() {
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
  } // testSeatHoldCreate()

  def testSeatHoldRelease() {
    val level = new Level(1, new LevelName("Test Name"), new MonetaryAmount(15.50), 4, 10)
    val rowNumber = new RowNumber(1)
    val seats = (1 to 3) map (sn => {new Seat(rowNumber, new SeatNumber(new Integer(sn)), level)})
    val sh = new SeatHold(email, toJava(Some(seats.asJava)))

    seats map { seat => level}

    println(s"\tSeats ${level.numSeatsAvailable}")

    //println(sh.getExpiration)
    val now = new Date
    val expiration = sh.getExpiration
    assert(expiration > now.getTime, s"Field ${expiration} <= ${now}")

    var size = sh.getSeats.size
    assert(size == seats.size, s"Failure on SeatHold getSeats, ${size} != ${seats.size}\n") 

    val resEmail = sh.getCustomerEmail
    assert(resEmail == email, s"Failure on SeatHold getCustomerEmail, ${resEmail} != ${email}\n")

    sh.release
    size = sh.getSeats.size
    assert(size == 0, s"Failure on SeatHold getSeats, ${size} != 0\n") 
  } // testSeatHoldRelease()


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
