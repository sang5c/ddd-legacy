package kitchenpos.fakeobject;

import kitchenpos.infra.ProfanityClient;

import java.util.List;

public class InMemoryProfanityClient implements ProfanityClient {

    private static final List<String> profanities = List.of("메롱");

    @Override
    public boolean containsProfanity(String text) {
        return profanities.stream()
                .anyMatch(text::contains);
    }
}
