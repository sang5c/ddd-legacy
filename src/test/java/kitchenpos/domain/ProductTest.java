package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @DisplayName("상품은 ID, 이름, 가격을 갖는다")
    @Test
    void create() {
        UUID id = UUID.randomUUID();
        String name = "후라이드치킨";
        BigDecimal price = BigDecimal.valueOf(20000);

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        assertThat(product).isNotNull();
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
    }
}
