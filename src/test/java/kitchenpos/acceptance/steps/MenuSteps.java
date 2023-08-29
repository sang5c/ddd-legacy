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
}
