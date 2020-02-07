package hackaton.fastdisision.config;

import hackaton.fastdisision.data.ClientResources;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.web.filter.CompositeFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OAuthConfig {

    private UserRepo userRepo;
    private final OAuth2ClientContext oauth2ClientContext;

    @Autowired
    public OAuthConfig(UserRepo userRepo, @Qualifier("oauth2ClientContext")OAuth2ClientContext oauth2ClientContext) {
        this.userRepo = userRepo;
        this.oauth2ClientContext = oauth2ClientContext;
    }

    @Bean
    @ConfigurationProperties("github")
    public ClientResources github() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("google")
    public ClientResources google() {
        return new ClientResources();
    }

    @Bean
    public PrincipalExtractor googlePrincipalExtractor(UserRepo userRepo) {
        return map -> {
            String id = String.valueOf(map.get("sub"));
            User user = userRepo.findById(id).orElseGet(() -> {
                User newUser = new User();
                newUser.setId(id);
                newUser.setUsername((String) map.get("name"));
                newUser.setEmail((String) map.get("email"));
                newUser.setUserPic((String) map.get("picture"));
                return newUser;
            });
            return userRepo.save(user);
        };
    }

    @Bean
    public PrincipalExtractor githubPrincipalExtractor(UserRepo userRepo) {
        return map -> {
            String id = String.valueOf(map.get("id"));
            User user = userRepo.findById(id).orElseGet(() -> {
                User newUser = new User();
                newUser.setId(id);
                newUser.setUsername((String) map.get("login"));
                newUser.setEmail(null);
                newUser.setUserPic((String) map.get("avatar_url"));
                return newUser;
            });
            return userRepo.save(user);
        };
    }

    @Bean
    public CompositeFilter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<OAuth2ClientAuthenticationProcessingFilter> filters = new ArrayList<>();
        filters.add(constructFilter(google(), "/login/google", googlePrincipalExtractor(userRepo)));
        filters.add(constructFilter(github(), "/login/github", githubPrincipalExtractor(userRepo)));
        filter.setFilters(filters);
        return filter;
    }

    private OAuth2ClientAuthenticationProcessingFilter constructFilter(ClientResources client, String path, PrincipalExtractor principalExtractor) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter.setRestTemplate(template);
        CustomUserInfoTokenServices tokenServices = new CustomUserInfoTokenServices(client.getResource().getUserInfoUri(), client.getClient().getClientId());
        tokenServices.setRestTemplate(template);
        tokenServices.setPrincipalExtractor(principalExtractor);
        filter.setTokenServices(tokenServices);
        return filter;
    }

}
