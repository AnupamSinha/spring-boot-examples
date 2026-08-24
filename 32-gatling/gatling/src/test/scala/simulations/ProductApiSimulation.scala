package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class ProductApiSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val getAllProducts = scenario("Get All Products")
    .exec(
      http("GET /api/products")
        .get("/api/products")
        .check(status.is(200))
        .check(jsonPath("$[0].id").exists)
        .check(responseTimeInMillis.lte(500))
    )

  val getProductById = scenario("Get Product By ID")
    .exec(
      http("GET /api/products/1")
        .get("/api/products/1")
        .check(status.is(200))
        .check(jsonPath("$.name").exists)
        .check(responseTimeInMillis.lte(200))
    )

  val createProduct = scenario("Create Product")
    .exec(
      http("POST /api/products")
        .post("/api/products")
        .body(StringBody(
          """{"name": "Load Test Product", "price": 29.99, "description": "Created during load test"}"""
        ))
        .check(status.is(201))
        .check(jsonPath("$.id").exists)
        .check(responseTimeInMillis.lte(500))
    )

  val mixedWorkload = scenario("Mixed Workload")
    .randomSwitch(
      60.0 -> exec(
        http("GET /api/products")
          .get("/api/products")
          .check(status.is(200))
      ),
      30.0 -> exec(
        http("GET /api/products/{id}")
          .get("/api/products/" + scala.util.Random.nextInt(50).toString)
          .check(status.in(200, 404))
      ),
      10.0 -> exec(
        http("POST /api/products")
          .post("/api/products")
          .body(StringBody(
            """{"name": "New Product", "price": 19.99, "description": "Mixed workload product"}"""
          ))
          .check(status.is(201))
      )
    )

  setUp(
    getAllProducts.inject(rampUsers(100).during(30.seconds)),
    getProductById.inject(rampUsers(100).during(30.seconds)),
    createProduct.inject(rampUsers(50).during(30.seconds)),
    mixedWorkload.inject(rampUsers(50).during(30.seconds))
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.percentile3.lt(1000),
      global.successfulRequests.percent.gt(95.0)
    )
}
