package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.acceptance.steps.ProductSteps.*;

class ProductAcceptanceTest extends AcceptanceTest {

    /**
     * Given
     * When  상품 등록을 요청 하면
     * Then  상품 등록이 성공한다.
     */
    @DisplayName("상품 등록")
    @Test
    void createProduct() {
        var 상품_등록_응답 = 상품_등록_요청("상품A", 1000);

        상품_등록_성공(상품_등록_응답);
    }

    /**
     * Given 상품을 등록하고
     * When  상품 가격 변경 요청하면
     * Then  가격 변경이 성공한다
     */
    @DisplayName("상품 가격 변경")
    @Test
    void changePrice() {
        var 상품_등록_응답 = 상품_등록_요청("상품A", 1000);
        String uri = 상품_등록_응답.header("Location");

        var 상품_가격_변경_응답 = 상품_가격_변경_요청(uri);

        상품_가격_변경_성공(상품_가격_변경_응답);
    }

    /**
     * Given 상품을 등록하고
     * And   다른 상품을 등록한 다음
     * When  상품을 목록을 조회하면
     * Then  등록한 상품 목록을 응답받는다
     */
    @DisplayName("상품 목록 조회")
    @Test
    void getProducts() {
        상품_등록_요청("짜장면", 1000);
        상품_등록_요청("짬뽕", 1000);

        ExtractableResponse<Response> 상품_목록_조회_응답 = 상품_목록_조회_요청();

        상품_목록_조회_성공(상품_목록_조회_응답, "짜장면", "짬뽕");
    }
}
