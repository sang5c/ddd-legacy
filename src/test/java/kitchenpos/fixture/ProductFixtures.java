package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductFixtures {

    public static Product createProduct(String name, BigDecimal price) {
        return createProduct(UUID.randomUUID(), name, price);
    }

    public static Product createProduct(UUID id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product createProductRequest(String name, BigDecimal price) {
        return createProduct(null, name, price);
    }
}
