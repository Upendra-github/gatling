
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
  val csvFeeder = csv("search.csv").circular // random is self explanatory, other options are .queue (error if run out) and .circular (goes back to start)

  def searchMyComputer() = {
    repeat(10) {
      // now call the feeder here
     feed(csvFeeder).
        exec(http("Computer database")
          .get("/computers?f=${searchCriterion}") // 4
          .check(css("a:contains('${searchComputerName}')", "href").saveAs("computerURL"))
        .check(status.is(200)))
        .pause(1)
    }
  }

  val scn = scenario("Search with CSV")
    .exec(searchMyComputer())

  setUp(scn.inject(atOnceUsers(5))).protocols(httpProtocol)
}
