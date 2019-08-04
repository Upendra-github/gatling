package myComputerCURD

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MyComputerCreate1 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://computer-database.gatling.io")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.disableAutoReferer
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0")

	val headers_0 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/ocsp-request")

	val headers_1 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Referer" -> "http://computer-database.gatling.io/computers",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_4 = Map(
		"Referer" -> "http://computer-database.gatling.io/computers/new",
		"Upgrade-Insecure-Requests" -> "1")

    val uri2 = "http://ocsp.digicert.com"

    val credentials = csv("data/userData.csv").circular

	val scn = scenario("MyComputerCRUD")
		// Launch
		.exec(http("request_0")
			.post(uri2 + "/")
			.headers(headers_0)
			.body(RawFileBody("/mycomputercrud/0000_request.dat")))
		.pause(6)
		.exec(http("request_1")
			.get("/computers")
			.headers(headers_1))
		.pause(33)
		// 2.AddComputer
		.exec(http("request_2")
			.get("/computers/new")
			.headers(headers_2))
		.pause(18)
		.exec(http("request_3")
			.post(uri2 + "/")
			.headers(headers_0)
			.body(RawFileBody("/mycomputercrud/0003_request.dat")))
		.pause(68)

		// 3.create this computer
		.feed(credentials)
		.exec(http("request_4")
			.post("/computers")
			.headers(headers_4)
			.formParam("name", "${userName}")
			.formParam("introduced", "${introduced}")
			.formParam("discontinued", "${discontinued}")
			.formParam("company", "1"))

	//setUp(scn.inject(splitUsers(10) into(nothingFor(4 seconds)) separatedBy(atOnceUsers(2)))).protocols(httpProtocol)
	//setUp(scn.inject(constantUsersPerSec(20) during (15 seconds))).protocols(httpProtocol)
	//setUp(scn.inject(rampUsersPerSec(1) to 5 during (5 seconds))).protocols(httpProtocol)
  //  setUp(scn.inject(rampUsers(2) over (5 seconds))).protocols(httpConf)
	setUp(scn.inject(atOnceUsers(5))).protocols(httpProtocol)
	//setUp(
    //  scn.inject(
       //nothingFor(4 seconds), // 1
        //atOnceUsers(10), // 2
       //rampUsers(10) over (5 seconds),
       //constantUsersPerSec(20) during (15 seconds), // 4
       //constantUsersPerSec(20) during (15 seconds) randomized, // 5
        //rampUsersPerSec(10) to 20 during (10 minutes), // 6
        //rampUsersPerSec(10) to 20 during (10 minutes) randomized, // 7
        //splitUsers(1000) into (rampUsers(10) over (10 seconds)) separatedBy (10 seconds), // 8
       // splitUsers(10) into (rampUsers(2) over (10 seconds)) separatedBy atOnceUsers(5), // 9
       // heavisideUsers(1000) over (20 seconds) // 1000
      //).protocols(httpConf)
    //)
}