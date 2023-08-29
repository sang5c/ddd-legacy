package kitchenpos.application;

import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.PurgomalumClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private PurgomalumClient purgomalumClient;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, menuRepository, purgomalumClient);
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

    private static Product createProductRequest(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

}
