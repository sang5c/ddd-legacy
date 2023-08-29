package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.infra.PurgomalumClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private PurgomalumClient purgomalumClient;

    @InjectMocks
    private ProductService productService;

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
        when(purgomalumClient.containsProfanity(any())).thenReturn(true);
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

        when(purgomalumClient.containsProfanity(any())).thenReturn(false);
        when(productRepository.save(any())).thenReturn(request);

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
        Product request = createProductRequest("한우", BigDecimal.valueOf(10000));
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.changePrice(UUID.randomUUID(), request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("상품 가격을 변경한다")
    @Test
    void changePrice() {
        BigDecimal price = BigDecimal.valueOf(10000);
        Product request = createProductRequest("한우", price);
        Product product = createProduct(UUID.randomUUID(), "한우", BigDecimal.valueOf(20000));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        Product changedProduct = productService.changePrice(product.getId(), request);

        assertThat(changedProduct.getPrice()).isEqualTo(price);
    }

    @DisplayName("상품 가격을 변경했을 때 메뉴 가격이 상품 가격 합보다 크면 메뉴를 숨긴다")
    @Test
    void hideMenuWhenMenuPriceIsGreaterThanProductPriceSum() {
        Product request = createProductRequest("한우", BigDecimal.valueOf(15000));
        Product product = createProduct(UUID.randomUUID(), "한우", BigDecimal.valueOf(20000));
        MenuProduct menuProduct = createMenuProduct(product, 1);
        Menu menu = createMenu(UUID.randomUUID(), BigDecimal.valueOf(20000), menuProduct);
        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(menuRepository.findAllByProductId(any())).willReturn(List.of(menu));

        productService.changePrice(product.getId(), request);

        assertThat(menu.isDisplayed()).isFalse();
    }

    private static Menu createMenu(UUID id, BigDecimal price, MenuProduct menuProduct) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setPrice(price);
        menu.setDisplayed(true);
        menu.setMenuProducts(List.of(menuProduct));
        return menu;
    }

    private static MenuProduct createMenuProduct(Product product, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(quantity);
        menuProduct.setProduct(product);
        return menuProduct;
    }

    private static Product createProductRequest(String name, BigDecimal price) {
        return createProduct(null, name, price);
    }

    private static Product createProduct(UUID id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

}
