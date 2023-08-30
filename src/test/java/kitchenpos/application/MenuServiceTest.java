package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.fakeobject.InMemoryMenuGroupRepository;
import kitchenpos.fakeobject.InMemoryMenuRepository;
import kitchenpos.fakeobject.InMemoryProductRepository;
import kitchenpos.fakeobject.InMemoryProfanityClient;
import kitchenpos.infra.ProfanityClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static kitchenpos.fixture.IdFixtures.INVALID_ID;
import static kitchenpos.fixture.MenuFixtures.createMenuProductRequest;
import static kitchenpos.fixture.MenuFixtures.createMenuRequest;
import static kitchenpos.fixture.MenuGroupFixtures.createMenuGroup;
import static kitchenpos.fixture.ProductFixtures.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuServiceTest {

    private MenuRepository menuRepository;
    private MenuGroupRepository menuGroupRepository;
    private ProductRepository productRepository;
    private ProfanityClient profanityClient;
    private MenuService menuService;

    @BeforeEach
    void setup() {
        menuRepository = new InMemoryMenuRepository();
        menuGroupRepository = new InMemoryMenuGroupRepository();
        productRepository = new InMemoryProductRepository();
        profanityClient = new InMemoryProfanityClient();
        menuService = new MenuService(menuRepository, menuGroupRepository, productRepository, profanityClient);
    }

    @DisplayName("가격이 비어있으면 등록할 수 없다")
    @Test
    void nullPrice() {
        Product product = productRepository.save(createProduct("후라이드 치킨", BigDecimal.valueOf(16000L)));
        MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("후라이드"));
        MenuProduct menuProductRequest = createMenuProductRequest(product.getId(), 1);
        Menu menu = createMenuRequest(menuGroup.getId(), null, menuProductRequest);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0원 이하이면 등록할 수 없다")
    @Test
    void negativePrice() {
        Product product = productRepository.save(createProduct("후라이드 치킨", BigDecimal.valueOf(16000L)));
        MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("후라이드"));
        MenuProduct menuProductRequest = createMenuProductRequest(product.getId(), 1);
        Menu menu = createMenuRequest(menuGroup.getId(), BigDecimal.valueOf(-1000), menuProductRequest);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 등록할 수 없다")
    @Test
    void notExistMenuGroup() {
        Product product = productRepository.save(createProduct("후라이드 치킨", BigDecimal.valueOf(16000L)));
        MenuProduct menuProductRequest = createMenuProductRequest(product.getId(), 1);
        Menu menu = createMenuRequest(INVALID_ID, BigDecimal.valueOf(16000L), menuProductRequest);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("상품이 없으면 메뉴를 등록할 수 없다")
    @ParameterizedTest
    @MethodSource("getMenuProducts")
    void emptyMenuProduct(List<MenuProduct> menuProducts) {
        MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("후라이드"));
        Menu menu = createMenuRequest(menuGroup.getId(), BigDecimal.valueOf(16000L), menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static List<Arguments> getMenuProducts() {
        return Arrays.asList(
                null,
                Arguments.of(Collections.emptyList()),
                Arguments.of(List.of(createMenuProductRequest(INVALID_ID, 2)))
        );
    }

    @DisplayName("메뉴 가격이 상품 가격 합보다 크면 등록할 수 없다")
    @Test
    void priceGreaterThanSum() {
        Product product = productRepository.save(createProduct("후라이드 치킨", BigDecimal.valueOf(16000L)));
        MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("후라이드"));
        MenuProduct menuProductRequest = createMenuProductRequest(product.getId(), 2);
        Menu menu = createMenuRequest(menuGroup.getId(), BigDecimal.valueOf(33000L), menuProductRequest);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 이름에 비속어가 포함되면 등록할 수 없다")
    @Test
    void containsProfanity() {
        Product product = productRepository.save(createProduct("후라이드 치킨", BigDecimal.valueOf(16000L)));
        MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("후라이드"));
        MenuProduct menuProductRequest = createMenuProductRequest(product.getId(), 1);
        Menu menu = createMenuRequest("메롱", menuGroup.getId(), BigDecimal.valueOf(16000L), menuProductRequest);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 이름은 비어있을 수 없다")
    @Test
    void nullName() {
        Product product = productRepository.save(createProduct("후라이드 치킨", BigDecimal.valueOf(16000L)));
        MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("후라이드"));
        MenuProduct menuProductRequest = createMenuProductRequest(product.getId(), 1);
        Menu menu = createMenuRequest(null, menuGroup.getId(), BigDecimal.valueOf(16000L), menuProductRequest);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() {
        Product product = productRepository.save(createProduct("후라이드 치킨", BigDecimal.valueOf(16000L)));
        MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("후라이드"));
        MenuProduct menuProductRequest1 = createMenuProductRequest(product.getId(), 1);
        MenuProduct menuProductRequest2 = createMenuProductRequest(product.getId(), 1);
        Menu menu = createMenuRequest("후라이드 두마리", menuGroup.getId(), BigDecimal.valueOf(16000L), menuProductRequest1, menuProductRequest2);

        Menu actual = menuService.create(menu);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(menu.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(menu.getPrice()),
                () -> assertThat(actual.getMenuGroup().getId()).isEqualTo(menu.getMenuGroupId())
        );
    }
}
