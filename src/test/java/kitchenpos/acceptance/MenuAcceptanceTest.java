package kitchenpos.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.acceptance.steps.MenuGroupSteps.메뉴_그룹_등록_요청;
import static kitchenpos.acceptance.steps.MenuSteps.메뉴_등록_성공;
import static kitchenpos.acceptance.steps.MenuSteps.메뉴_등록_요청;
import static kitchenpos.acceptance.steps.ProductSteps.상품_등록_요청;

class MenuAcceptanceTest extends AcceptanceTest {

    /**
     * Given 상품을 등록하고
     * And   메뉴 그룹을 등록한 다음
     * When  메뉴를 등록하면
     * Then  메뉴가 등록된다
     */
    @DisplayName("메뉴 등록")
    @Test
    void createMenu() {
        var 상품_한우 = 상품_등록_요청("한우", 20000);
        var 한우_ID = 상품_한우.jsonPath().getString("id");
        var 메뉴_그룹_한식 = 메뉴_그룹_등록_요청("한식");
        var 한식_ID = 메뉴_그룹_한식.jsonPath().getString("id");

        var 메뉴_등록_응답 = 메뉴_등록_요청(한식_ID, 한우_ID, 20000);

        메뉴_등록_성공(메뉴_등록_응답);
    }
}
