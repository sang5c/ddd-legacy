package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.acceptance.steps.MenuGroupSteps.*;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    /**
     * Given
     * When  메뉴 그룹을 등록 요청하면
     * Then  메뉴 그룹이 등록된다
     */
    @DisplayName("메뉴 그룹 등록")
    @Test
    void createMenuGroup() {
        ExtractableResponse<Response> 한우_그룹_응답 = 메뉴_그룹_등록_요청("한우 그룹");

        메뉴_그룹_등록_성공(한우_그룹_응답);
    }

    /**
     * Given 메뉴 그룹을 등록하고
     * When  메뉴 그룹 목록 조회 요청하면
     * Then  메뉴 그룹 목록을 응답 받는다
     */
    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void getMenuGroups() {
        메뉴_그룹_등록_요청("한우 그룹");
        메뉴_그룹_등록_요청("닭고기 그룹");

        var 메뉴_그룹_목록_조회_응답 = 메뉴_그룹_목록_조회_요청();

        메뉴_그룹_목록_조회_성공(메뉴_그룹_목록_조회_응답, "한우 그룹", "닭고기 그룹");
    }
}
