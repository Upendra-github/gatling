package myComputerCURD

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MyComputerSearchUpdate extends Simulation {

	val httpProtocol = http
		.baseUrl("http://computer-database.gatling.io")
		.disableFollowRedirect
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.disableAutoReferer
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0")

	val headers_2 = Map("Referer" -> "http://computer-database.gatling.io/computers")

	val headers_3 = Map("Referer" -> "http://computer-database.gatling.io/computers?f=Apple+6u")

	val headers_4 = Map("Referer" -> "http://computer-database.gatling.io/computers/1040")



	val scn = scenario("MyComputerSearchUpdate")
		// Launch URL
		.exec(http("request_0")
			.get("/")
			.resources(http("request_1")
			.get("/computers"))
			.check(status.is(303)))
		.pause(47)
		// 2 Search Computer
		.exec(http("request_2")
			.get("/computers?f=Apple+6u")
			.headers(headers_2))
		.pause(30)
		// 3 Click on Computer name link
		.exec(http("request_3")
			.get("/computers/1040")
			.headers(headers_3))
		.pause(73)
		// 4 Click on Save this computer
		.exec(http("request_4")
			.post("/computers/1040")
			.headers(headers_4)
			.formParam("name", "Upendra 6U")
			.formParam("introduced", "2019-08-04")
			.formParam("discontinued", "2022-09-22")
			.formParam("company", "1")
			.resources(http("request_5")
			.get("/computers")
			.headers(headers_4))
			.check(status.is(303)))

	setUp(scn.inject(atOnceUsers(10))).protocols(httpProtocol)
}