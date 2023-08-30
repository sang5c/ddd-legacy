package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.fakeobject.InMemoryMenuRepository;
import kitchenpos.fakeobject.InMemoryProductRepository;
import kitchenpos.fakeobject.InMemoryProfanityClient;
import kitchenpos.infra.ProfanityClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static kitchenpos.fixture.MenuFixtures.createMenu;
import static kitchenpos.fixture.MenuFixtures.createMenuProduct;
import static kitchenpos.fixture.ProductFixtures.createProduct;
import static kitchenpos.fixture.ProductFixtures.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductServiceTest {

    private ProductRepository productRepository;
    private MenuRepository menuRepository;
    private ProfanityClient profanityClient;
    private ProductService productService;

    @BeforeEach
    void setup() {
        productRepository = new InMemoryProductRepository();
        menuRepository = new InMemoryMenuRepository();
        profanityClient = new InMemoryProfanityClient();
        productService = new ProductService(productRepository, menuRepository, profanityClient);
    }

    @DisplayName("이름이 비어있으면 에러가 발생한다")
    @Test
    void createWithInvalidName() {
        Product request = createProductRequest(null, BigDecimal.valueOf(10000));

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름에 욕설이 포함되어 있으면 에러가 발생한다")
    @Test
    void createWithProfanityName() {
        Product request = createProductRequest("메롱", BigDecimal.valueOf(10000));

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0보다 작으면 에러가 발생한다")
    @Test
    void createWithNegativePrice() {
        Product request = createProductRequest("한우", BigDecimal.valueOf(-10000));

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 비어있으면 에러가 발생한다")
    @Test
    void createWithEmptyPrice() {
        Product request = createProductRequest("한돈", null);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름과 가격을 받아 상품을 등록한다")
    @Test
    void create() {
        String name = "한우";
        BigDecimal price = BigDecimal.valueOf(10000);
        Product request = createProductRequest(name, price);

        Product product = productService.create(request);

        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
    }

    @DisplayName("가격 변경시 상품 가격이 비어있거나 0보다 작으면 예외가 발생한다")
    @Test
    void changePriceWithEmptyPrice() {
        Product request = createProductRequest("한우", null);

        assertThatThrownBy(() -> productService.changePrice(UUID.randomUUID(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 변경시 상품 가격이 0보다 작으면 예외가 발생한다")
    @Test
    void changePriceWithNegativePrice() {
        Product request = createProductRequest("한우", BigDecimal.valueOf(-10000));

        assertThatThrownBy(() -> productService.changePrice(UUID.randomUUID(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 변경시 상품이 존재하지 않으면 예외가 발생한다")
    @Test
    void changePriceWithNotExistProduct() {
        Product request = productRepository.save(createProductRequest("한우", BigDecimal.valueOf(10000)));

        assertThatThrownBy(() -> productService.changePrice(UUID.randomUUID(), request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("상품 가격을 변경한다")
    @Test
    void changePrice() {
        Product savedProduct = productRepository.save(createProduct("한우", BigDecimal.valueOf(20000)));
        BigDecimal price = BigDecimal.valueOf(10000);
        Product request = createProductRequest("한우", price);

        Product changedProduct = productService.changePrice(savedProduct.getId(), request);

        assertThat(changedProduct.getPrice()).isEqualTo(price);
    }

    @DisplayName("상품 가격을 변경했을 때 메뉴 가격이 상품 가격 합보다 크면 메뉴를 숨긴다")
    @Test
    void hideMenuWhenMenuPriceIsGreaterThanProductPriceSum() {
        Product product = productRepository.save(createProduct("한우", BigDecimal.valueOf(20000)));
        Menu menu = menuRepository.save(createMenu(BigDecimal.valueOf(20000), createMenuProduct(product, 1)));
        Product request = createProductRequest("한우", BigDecimal.valueOf(15000));

        productService.changePrice(product.getId(), request);

        assertThat(menu.isDisplayed()).isFalse();
    }

    @DisplayName("상품 목록을 조회할 수 있다")
    @Test
    void list() {
        Product product = productRepository.save(createProduct("한우", BigDecimal.valueOf(20000)));

        List<Product> products = productService.findAll();

        assertThat(products).containsExactly(product);
    }
}
