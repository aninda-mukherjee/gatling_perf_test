
import com.update.transactions._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

/**
 * Example Gatling load test that sends one HTTP GET requests to a URL.

 * Run this maven command to run gatling simulation wih default values.
 * mvn gatling:test -Dgatling.simulationClass=perfTestSimulation
 *
 * Run this maven command to run gatling simulation with parameter values.
 * mvn gatling:test
 *      -Dgatling.simulationClass=perfTestSimulation  // For Simulation class
 *      -Durl=http://localhost:8080  // Base URL
 *      -DnUsers=10 // concurrent user
 *      -DrampUpTime=5 // Ramp-up time in minutes.
 *      -DnDuration=10 // Steady state time in minutes.
 *      -DnoOfUsersCreation=100 // Number of users create for data seeding.
 *
 *
 * @author XXXXXX
 */

class perfTestSimulation extends Simulation{

  var BASE_URL = System.getProperty("url", "http://localhost:8080")
  //Number of users. Default is 1
  var nUsers: Double = System.getProperty("nUsers", "10").toDouble
  // Peak trasaction per sec. Default 1 per sec.
  var nDurationRamp: Double = System.getProperty("rampUpTime", "1").toDouble
  // Time to ramp up load from 0 to peak load. Default 1 min.
  var nDuration: Double = System.getProperty("steadyStateTime", "5").toDouble
  //Number of users to create
  var noOfUsersCreation: Double = System.getProperty("noOfUsersCreation", "10").toDouble

  var nUsers_PO = nUsers * 0.05 // Multiply user concurrently.

  //Print variables before starting test.
  println("BASE_URL=" + BASE_URL)
  println("nUsers=" + nUsers)
  println("nDuration=" + nDuration)
  println("nDurationRamp=" + nDurationRamp)


  val httpProtocol = http
      .baseUrl(BASE_URL)
      .acceptHeader("*/*")
      .acceptEncodingHeader("gzip, deflate, sdch")
      .acceptLanguageHeader("en-US,en;q=0.8")
      .maxRedirects(4: Int)
      .header("Accept-Language","en-US")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.86 Safari/537.36")


  /*******************************************************
   * Concurrent createUser and getUserById requests Scenario
   ******************************************************/

  val createAndGetUserGroup = scenario("createAndGetUserGroup")
      .exec(createUser.createUser_Transaction).exitHereIfFailed // Exit if this transaction fails.
      .pause(1 second, 5 second) // Pause 1 Second to 5 Seconds
      .exec(getUserByID.getUserByID_Transaction)

  /*******************************************************
   * Generate 100+ users in the database Scenario
   ******************************************************/

  val createUserGroup = scenario("createUserGroup")
    .exec(createUser.createUser_Transaction).exitHereIfFailed // Exit if this transaction fails.

  /*****************************************************************
   * Use below inject simulation group for Baseline Tests. It will ramp-up within nDuration and then run
   * with nUsers_PO constant users for nDuration minutes.
   ****************************************************************/
  setUp(
    createUserGroup.inject(
        rampUsers(noOfUsersCreation.toInt) during(nDuration.toDouble minutes)
    ),
    createAndGetUserGroup.inject(
      rampUsersPerSec(0.1) to nUsers_PO during(nDurationRamp minutes),
      constantUsersPerSec(nUsers_PO) during(nDuration minutes)
//      atOnceUsers(1)
    )
    ).protocols(httpProtocol)

  /*****************************************************************
   * Use below inject simulation group for Throughput Based Tests.
   ****************************************************************/

//  setUp(createAndGetUserGroup.inject(
//    constantUsersPerSec(100) during(10 minutes)
//      )).throttle(reachRps(200) in (10 minutes), holdFor(2 minutes))
//        .protocols(httpProtocol)



}
