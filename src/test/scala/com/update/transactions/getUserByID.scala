package com.update.transactions

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object getUserByID {

  val logger = org.slf4j.LoggerFactory.getLogger("shellPerfLogger")

  val getUserByID_Transaction=
        exec(session=>{
          var headerString=""
//          headerString=util.processTidHeader("na",getClass.toString())
//          logger.debug("upgrade_tid:"+headerString)
          session.set("upgrade_tid",headerString)
        })
        .exec(
        http("get_userById")
          .get("/user/v1/find/d61ead34-3d8c-4ef0-813e-6420bdd10a87")
          .header("Cache-Control", "no-cache")
          .header("Accept-Language", "en-US")
          .header("Accept", "*/*")
          .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko")
          .header("Accept-Encoding", "gzip, deflate")

          .check(status.in(200))
          .check(responseTimeInMillis.saveAs("execLatency"))
//          .check(regex("serverGroupCompanyId\":\"(.*?)\",").saveAs("CoID"))

      )


}