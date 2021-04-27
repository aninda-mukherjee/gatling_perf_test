package com.update.transactions

import com.github.javafaker.Faker
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object createUser {

  val logger = org.slf4j.LoggerFactory.getLogger("shellPerfLogger")


  val createUser_Transaction=
        exec(session=>{
          var headerString=""
//          headerString=util.processTidHeader("na",getClass.toString())
//          logger.debug("upgrade_tid:"+headerString)
          val faker = new Faker
          val firstName=faker.name.firstName()
          val lastName = faker.name.lastName()
          val email = faker.name.username() + "@mail.com"

          var payload = "{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"email\":\"" + email + "\"}";
          session.set("upgrade_tid",headerString).set("payload", payload)
        })
        .exec(
        http("post_createUser")
          .post("/user/v1/create")
          .header("Content-Type", "application/json")
          .header("Accept", "*/*")
          .header("Accept-Encoding", "gzip, deflate")
//          .header("Connection", "keep-alive")
//          .header("tid","222-22-222")
          .body(StringBody("""${payload}"""))

          .check(status.in(201))
          .check(responseTimeInMillis.saveAs("execLatency"))
          .check(bodyString.saveAs("responsePayload"))
          .check(jsonPath("$.id").saveAs("p_uuid"))
      )
        .exec(session=>{
          val response=session("responsePayload").as[String];
          val uuid=session("p_uuid").as[String];
          val latency=session("execLatency").as[Integer].toString();

          logger.info("=======POST CREATE USER  Response=========")
          logger.info("======>> LATENCY: "+latency+" miliseconds")
          logger.info("======>> RESPONSE: "+response+ "message")
          logger.info("======>> USER UUID: "+uuid)
          session;

        })



}