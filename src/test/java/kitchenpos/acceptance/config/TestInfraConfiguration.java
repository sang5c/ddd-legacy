package kitchenpos.acceptance.config;

import kitchenpos.fakeobject.InMemoryProfanityClient;
import kitchenpos.infra.ProfanityClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestInfraConfiguration {

    @Bean
    public ProfanityClient profanityClient() {
        return new InMemoryProfanityClient();
    }

}
