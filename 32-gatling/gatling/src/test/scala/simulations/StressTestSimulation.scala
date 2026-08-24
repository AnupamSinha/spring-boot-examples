package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class StressTestSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  // Stress test: constant high load to find breaking point
  val stressScenario = scenario("Stress Test - Constant Load")
    .exec(
      http("GET /api/products")
        .get("/api/products")
        .check(status.is(200))
    )
    .pause(100.milliseconds, 500.milliseconds)
    .exec(
      http("GET /api/products/1")
        .get("/api/products/1")
        .check(status.is(200))
    )

  // Spike test: sudden burst of traffic
  val spikeScenario = scenario("Spike Test")
    .exec(
      http("GET /api/products (spike)")
        .get("/api/products")
        .check(status.is(200))
    )

  // Soak test: sustained load over time
  val soakScenario = scenario("Soak Test")
    .exec(
      http("GET /api/products (soak)")
        .get("/api/products")
        .check(status.is(200))
    )
    .pause(500.milliseconds, 2.seconds)
    .exec(
      http("POST /api/products (soak)")
        .post("/api/products")
        .body(StringBody(
          """{"name": "Soak Product", "price": 9.99, "description": "Soak test"}"""
        ))
        .check(status.is(201))
    )

  setUp(
    stressScenario.inject(
      constantUsersPerSec(50).during(60.seconds)
    ),
    spikeScenario.inject(
      nothingFor(10.seconds),
      atOnceUsers(200),
      nothingFor(20.seconds),
      atOnceUsers(200)
    ),
    soakScenario.inject(
      constantUsersPerSec(10).during(120.seconds)
    )
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.percentile3.lt(2000),
      global.failedRequests.percent.lt(10.0)
    )
}
