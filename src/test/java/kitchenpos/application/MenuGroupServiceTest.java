package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.fixture.MenuGroupFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void create() {
        MenuGroup menuGroup = MenuGroupFixtures.createMenuGroup("그룹1");
        given(menuGroupRepository.save(any())).willReturn(menuGroup);

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
        MenuGroup menuGroup1 = MenuGroupFixtures.createMenuGroup("그룹1");
        MenuGroup menuGroup2 = MenuGroupFixtures.createMenuGroup("그룹2");
        given(menuGroupRepository.findAll()).willReturn(List.of(menuGroup1, menuGroup2));

        List<MenuGroup> groups = menuGroupService.findAll();
        
        assertThat(groups).containsExactly(menuGroup1, menuGroup2);
    }
}
