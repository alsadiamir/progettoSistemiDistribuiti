package it.unibo.canteen.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleConfig {

    @Bean
    public GoogleJwtVerifier googleJwtVerifier() {
        return new GoogleJwtVerifier();
    }
}
