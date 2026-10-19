package services;

import static io.restassured.RestAssured.given;

public class HealthService extends BaseService {

    // O /health devolve texto puro ("Healthy"), por isso não usa accept JSON
    public void verificarSaude(String endPoint) {
        response = given()
                .when()
                .get(baseUrl + endPoint)
                .then()
                .extract()
                .response();
    }
}
