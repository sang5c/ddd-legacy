package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MenuFixtures {

    public static Menu createMenuRequest(UUID menuGroupId, BigDecimal price, MenuProduct... menuProducts) {
        return createMenuRequest(menuGroupId, price, Arrays.asList(menuProducts));
    }

    public static Menu createMenuRequest(UUID menuGroupId, BigDecimal price, List<MenuProduct> menuProductRequests) {
        return createMenuRequest("후라이드 메뉴", menuGroupId, price, menuProductRequests);
    }

    public static Menu createMenuRequest(String name, UUID menuGroupId, BigDecimal price, MenuProduct... menuProducts) {
        return createMenuRequest(name, menuGroupId, price, Arrays.asList(menuProducts));
    }

    public static Menu createMenuRequest(String name, UUID menuGroupId, BigDecimal price, List<MenuProduct> menuProductRequests) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProductRequests);
        return menu;
    }

    public static Menu createMenu(BigDecimal price, MenuProduct menuProduct) {
        return createMenu(UUID.randomUUID(), price, menuProduct);
    }

    public static Menu createMenu(UUID id, BigDecimal price, MenuProduct menuProduct) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setPrice(price);
        menu.setDisplayed(true);
        menu.setMenuProducts(List.of(menuProduct));
        return menu;
    }

    public static MenuProduct createMenuProduct(Product product, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(quantity);
        menuProduct.setProduct(product);
        return menuProduct;
    }

    public static MenuProduct createMenuProductRequest(UUID productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
