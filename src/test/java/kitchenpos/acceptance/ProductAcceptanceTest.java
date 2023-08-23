package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.acceptance.ProductSteps.*;

class ProductAcceptanceTest extends AcceptanceTest {

    /**
     * When 상품 등록을 요청 하면
     * Then 상품 등록이 성공한다.
     */
    @DisplayName("상품 등록")
    @Test
    void createProduct() {
        var 상품_등록_응답 = 상품_등록_요청();

        상품_등록_성공(상품_등록_응답);
    }

    /**
     * Given 상품을 등록하고
     * When 상품 가격 변경 요청하면
     * Then 가격 변경이 성공한다
     */
    @DisplayName("상품 가격 변경")
    @Test
    void changePrice() {
        ExtractableResponse<Response> 상품_등록_응답 = 상품_등록_요청();
        String uri = 상품_등록_응답.header("Location");

        var 상품_가격_변경_응답 = 상품_가격_변경_요청(uri);

        상품_가격_변경_성공(상품_가격_변경_응답);
    }
}
