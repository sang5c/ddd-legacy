package kitchenpos.acceptance.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductSteps {
    public static ExtractableResponse<Response> 상품_등록_요청(String name, BigDecimal price) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("price", price);

        return RestAssured
                .given().log().all()
                .body(body)
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

    public static ExtractableResponse<Response> 상품_가격_변경_요청(String uri) {
        return RestAssured
                .given().log().all()
                .body("{\"price\":2000}")
                .contentType("application/json")
                .when().log().all()
                .put(uri + "/price")
                .then().log().all()
                .extract();
    }

    public static void 상품_가격_변경_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().log().all()
                .get("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_목록_조회_성공(ExtractableResponse<Response> response, String... names) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).containsExactly(names);
    }
}
