package kitchenpos.acceptance.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupSteps {
    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);

        return RestAssured
                .given().log().all()
                .body(body)
                .contentType("application/json")
                .when().log().all()
                .post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_그룹_등록_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().log().all()
                .get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_그룹_목록_조회_성공(ExtractableResponse<Response> response, String... names) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).containsExactly(names);
    }
}
