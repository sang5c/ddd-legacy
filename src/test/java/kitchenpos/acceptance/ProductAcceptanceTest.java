package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.acceptance.ProductSteps.상품_등록_성공;
import static kitchenpos.acceptance.ProductSteps.상품_등록_요청;

class ProductAcceptanceTest extends AcceptanceTest {

    /**
     * When 상품 등록을 요청 하면
     * Then 상품 등록이 성공한다.
     */
    @DisplayName("상품 등록")
    @Test
    void createProduct() {
        ExtractableResponse<Response> 상품_등록_응답 = 상품_등록_요청();

        상품_등록_성공(상품_등록_응답);
    }
}
