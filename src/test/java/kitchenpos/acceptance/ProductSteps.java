package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ProductSteps {
    public static ExtractableResponse<Response> 상품_등록_요청() {
        return RestAssured
                .given().log().all()
                .body("{\"name\":\"상품A\",\"price\":1000}")
                .contentType("application/json")
                .when().log().all()
                .post("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_등록_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
