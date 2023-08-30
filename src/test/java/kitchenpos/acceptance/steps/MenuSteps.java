package kitchenpos.acceptance.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuSteps {
    public static ExtractableResponse<Response> 메뉴_등록_요청(String menuGroupId, String productId, int menuPrice) {
        Map<String, Object> menuProducts = new HashMap<>();
        menuProducts.put("productId", productId);
        menuProducts.put("quantity", 1);

        Map<String, Object> body = new HashMap<>();
        body.put("name", "후라이드+후라이드");
        body.put("price", menuPrice);
        body.put("menuGroupId", menuGroupId);
        body.put("displayed", true);
        body.put("menuProducts", List.of(menuProducts));

        return RestAssured
                .given().log().all()
                .body(body)
                .contentType("application/json")
                .when().log().all()
                .post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_등록_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 메뉴_가격_변경_요청(String menuId, int menuPrice) {
        Map<String, Object> body = new HashMap<>();
        body.put("price", menuPrice);

        return RestAssured
                .given().log().all()
                .body(body)
                .contentType("application/json")
                .when().log().all()
                .put("/api/menus/" + menuId + "/price")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_가격_변경_성공(ExtractableResponse<Response> response, int price) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getLong("price")).isEqualTo(price);
    }

    public static ExtractableResponse<Response> 메뉴_노출_요청(String menuId) {
        return RestAssured
                .given().log().all()
                .when().log().all()
                .put("/api/menus/" + menuId + "/display")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_노출_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getBoolean("displayed")).isTrue();
    }

    public static ExtractableResponse<Response> 메뉴_숨김_요청(String menuId) {
        return RestAssured
                .given().log().all()
                .when().log().all()
                .put("/api/menus/" + menuId + "/hide")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_숨김_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getBoolean("displayed")).isFalse();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().log().all()
                .get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_목록_조회_성공(ExtractableResponse<Response> response, String... menuIds) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("id", String.class)).containsExactly(menuIds);
    }
}
