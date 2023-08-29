package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @DisplayName("메뉴 상품은 상품 정보와 수량을 갖는다")
    @Test
    void create() {
        Product product = new Product();
        long quantity = 1L;

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProduct(product);
        menuProduct.setQuantity(quantity);

        assertThat(menuProduct.getProduct()).isEqualTo(product);
        assertThat(menuProduct.getQuantity()).isEqualTo(quantity);
    }
}
