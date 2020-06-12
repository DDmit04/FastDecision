package hackaton.fastdisision.config.auth;

import hackaton.fastdisision.data.ClientResources;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 **/
@Configuration
public class OAuthResourcesConfiguration {

    @Bean
    @ConfigurationProperties("github")
    public ClientResources githubOAuthResource() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("google")
    public ClientResources googleOAuthResource() {
        return new ClientResources();
    }

}
