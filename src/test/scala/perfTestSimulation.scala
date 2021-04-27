import com.update.transactions._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class perfTestSimulation extends Simulation{

  var BASE_URL = System.getProperty("url", "http://localhost:8080")
  //Number of users. Default is 1
  var nUsers: Double = System.getProperty("peakTPS", "1000").toDouble
  // Peak trasaction per sec. Default 1 per sec.
  var nDurationRamp: Double = System.getProperty("rampUpTime", "1").toDouble
  // Time to ramp up load from 0 to peak load. Default 1 min.
  var nDuration: Double = System.getProperty("steadyStateTime", "2").toDouble // Duration to run the test at Peak load. Default is 1 min.
  var nUsers_PO = nUsers * 0.05

  //Print variables
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
//      .header("Accept","text/html, application/xhtml+xml, */*")
//      .contentTypeHeader("application/x-www-form-urlencoded;charset=UTF-8")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.86 Safari/537.36")

  val createUserGroup = scenario("createUserGroup")

      .exec(createUser.createUser_Transaction)
//      .pause(10)
  //    .exec(getUserByID.getUserByID_Transaction)


  setUp(
    createUserGroup.inject(
//      rampUsersPerSec(0.1) to nUsers_PO during(nDurationRamp minutes),
//      constantUsersPerSec(nUsers_PO) during(nDuration minutes)
      atOnceUsers(1)
    )
    ).protocols(httpProtocol)



}
