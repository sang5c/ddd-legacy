package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.fakeobject.InMemoryMenuGroupRepository;
import kitchenpos.fixture.MenuGroupFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuGroupServiceTest {

    private MenuGroupRepository menuGroupRepository;
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setup() {
        menuGroupRepository = new InMemoryMenuGroupRepository();
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void create() {
        MenuGroup menuGroup = MenuGroupFixtures.createMenuGroup("그룹1");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("그룹 이름이 없으면 예외가 발생한다")
    @ParameterizedTest
    @NullAndEmptySource
    void createWithEmptyName(String name) {
        MenuGroup menuGroup = MenuGroupFixtures.createMenuGroup(name);

        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void findAll() {
        menuGroupRepository.save(MenuGroupFixtures.createMenuGroup("그룹1"));
        menuGroupRepository.save(MenuGroupFixtures.createMenuGroup("그룹2"));

        List<MenuGroup> groups = menuGroupService.findAll();

        assertThat(groups).hasSize(2);
    }
}
