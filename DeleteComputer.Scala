package myComputerCURD

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MyComputerSearchDelete extends Simulation {

	val httpProtocol = http
		.baseUrl("http://computer-database.gatling.io")
		.disableFollowRedirect
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.disableAutoReferer
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Referer" -> "http://computer-database.gatling.io/computers",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Referer" -> "http://computer-database.gatling.io/computers?f=Apple+6U",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_3 = Map(
		"Referer" -> "http://computer-database.gatling.io/computers/1040",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_5 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/ocsp-request")

    val uri1 = "http://ocsp.pki.goog/GTSGIAG3"

    val credentials = csv("data/userData.csv").random

	val scn = scenario("MyComputerSearchDelete")
		// 1 Launch Url
		.exec(http("request_0")
			.get("/computers")
			.headers(headers_0))
		.pause(31)
		// 2 Search Computer

		.feed(credentials)
		.exec(http("request_1")
			.get("/computers?f=${userName}")
			.check(
			regex("""<a href="/computers/(.*)">""")
			.find
			.saveAs(key = "deletedComputerNo") //1040
			)
			.headers(headers_1))
		.pause(25)
		// 3 Click on Computername
		.exec(http("request_2")
			.get("/computers/${deletedComputerNo}")
			.headers(headers_2))
		.pause(61)
		// 4 Delete computer
		.exec(http("request_3")
			.post("/computers/${deletedComputerNo}/delete")
			.headers(headers_3)
			.resources(http("request_4")
			.get("/computers")
			.headers(headers_3))
			.check(status.is(303)))
		.pause(27)
		.exec(http("request_5")
			.post(uri1)
			.headers(headers_5)
			.body(RawFileBody("myComputerCURD/mycomputersearchdelete/0005_request.dat")))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
