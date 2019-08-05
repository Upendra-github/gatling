import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CsvFeeder extends Simulation {
  val httpProtocol = http
    .baseUrl("http://computer-database.gatling.io")
    .inferHtmlResources()
    .acceptHeader("image/webp,*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0")

  // this reads the csv file in src/gatling/resources/data
  var idNumbers = (1 to 10).iterator
  val customFeeder = Iterator.continually(Map("testId" -> idNumber.next()))

  def searchMyComputer() = {

      // now call the feeder here
      feed(customFeeder).
        exec(http("Computer database")
          .get("/computers/${testId}") // 4
          .check(css("a:contains('${testId}')", "href").saveAs("computerURL"))
          .check(status.is(200)))
        .pause(1)

  }

  val scn = scenario("Search with CSV")
    .exec(searchMyComputer())

  setUp(scn.inject(atOnceUsers(5))).protocols(httpProtocol)
}
