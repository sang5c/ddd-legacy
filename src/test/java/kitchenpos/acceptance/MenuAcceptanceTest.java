package kitchenpos.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.acceptance.steps.MenuGroupSteps.메뉴_그룹_등록_요청;
import static kitchenpos.acceptance.steps.MenuSteps.*;
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
        var 한우_ID = 상품_등록_요청("한우", 20000).jsonPath().getString("id");
        var 한식_ID = 메뉴_그룹_등록_요청("한식").jsonPath().getString("id");

        var 메뉴_등록_응답 = 메뉴_등록_요청(한식_ID, 한우_ID, 20000);

        메뉴_등록_성공(메뉴_등록_응답);
    }

    /**
     * Given 상품을 등록하고
     * And   메뉴 그룹을 등록한 다음
     * And   메뉴를 등록하고
     * When  메뉴 가격을 변경하면
     * Then  메뉴 가격이 변경된다
     */
    @DisplayName("메뉴 가격 변경")
    @Test
    void changeMenuPrice() {
        var 한우_ID = 상품_등록_요청("한우", 20000).jsonPath().getString("id");
        var 한식_ID = 메뉴_그룹_등록_요청("한식").jsonPath().getString("id");
        var 메뉴_ID = 메뉴_등록_요청(한식_ID, 한우_ID, 20000).jsonPath().getString("id");

        var 메뉴_가격_변경_응답 = 메뉴_가격_변경_요청(메뉴_ID, 19000);

        메뉴_가격_변경_성공(메뉴_가격_변경_응답, 19000);
    }

    /**
     * Given 상품을 등록하고
     * And   메뉴 그룹을 등록한 다음
     * And   메뉴를 등록하고
     * When  메뉴를 노출하도록 하면
     * Then  노출 처리된다
     */
    @DisplayName("메뉴 노출")
    @Test
    void displayMenu() {
        var 한우_ID = 상품_등록_요청("한우", 20000).jsonPath().getString("id");
        var 한식_ID = 메뉴_그룹_등록_요청("한식").jsonPath().getString("id");
        var 메뉴_ID = 메뉴_등록_요청(한식_ID, 한우_ID, 20000).jsonPath().getString("id");

        var 메뉴_노출_응답 = 메뉴_노출_요청(메뉴_ID);

        메뉴_노출_성공(메뉴_노출_응답);
    }

    /**
     * Given 상품을 등록하고
     * And   메뉴 그룹을 등록한 다음
     * And   메뉴를 등록하고
     * When  메뉴를 숨기도록 하면
     * Then  숨김 처리된다
     */
    @DisplayName("메뉴 숨김")
    @Test
    void hideMenu() {
        var 한우_ID = 상품_등록_요청("한우", 20000).jsonPath().getString("id");
        var 한식_ID = 메뉴_그룹_등록_요청("한식").jsonPath().getString("id");
        var 메뉴_ID = 메뉴_등록_요청(한식_ID, 한우_ID, 20000).jsonPath().getString("id");

        var 메뉴_숨김_응답 = 메뉴_숨김_요청(메뉴_ID);

        메뉴_숨김_성공(메뉴_숨김_응답);
    }
}
