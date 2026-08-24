import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name "should return payment by id"
    description "When GET /api/payments/1 is called, return payment details"

    request {
        method GET()
        url "/api/payments/1"
        headers {
            contentType(applicationJson())
        }
    }

    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(
            id: 1,
            orderId: "ORD-2024-001",
            amount: 99.99,
            currency: "USD",
            status: "COMPLETED"
        )
        bodyMatchers {
            jsonPath('$.id', byType())
            jsonPath('$.orderId', byRegex('[A-Z]{3}-\\d{4}-\\d{3}'))
            jsonPath('$.amount', byType())
            jsonPath('$.currency', byRegex('[A-Z]{3}'))
            jsonPath('$.status', byRegex('COMPLETED|PENDING|FAILED'))
        }
    }
}
