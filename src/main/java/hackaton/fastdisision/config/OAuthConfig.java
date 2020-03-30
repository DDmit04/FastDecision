package hackaton.fastdisision.config;

import hackaton.fastdisision.data.ClientResources;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRoles;
import hackaton.fastdisision.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.web.filter.CompositeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Configuration of web security OAuth filters
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
@Configuration
public class OAuthConfig {

    @Value("${spring.profiles.active:prod}")
    private String profile;

    private final UserRepo userRepo;
    private final OAuth2ClientContext oauth2ClientContext;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OAuthConfig(UserRepo userRepo, @Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.oauth2ClientContext = oauth2ClientContext;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @ConfigurationProperties("github")
    public ClientResources github() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("github-dev")
    public ClientResources githubDev() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("google")
    public ClientResources google() {
        return new ClientResources();
    }

    /**
     * Extract google OAuth response to user
     * @param userRepo repo to save user
     * @return data extractor
     * @see UserRepo
     */
    @Bean
    public PrincipalExtractor googlePrincipalExtractor(UserRepo userRepo) {
        return map -> {
            String id = String.valueOf(map.get("sub"));
            User user = userRepo.findById(id).orElseGet(() -> {
                User newUser = new User();
                newUser.setId(id);
                newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                newUser.getRoles().add(UserRoles.USER);
                newUser.setUsername((String) map.get("name"));
                newUser.setEmail((String) map.get("email"));
                newUser.setUserPic((String) map.get("picture"));
                return newUser;
            });
            return userRepo.save(user);
        };
    }

    /**
     * Extract github OAuth response to user
     * @param userRepo repo to save user
     * @return data extractor
     * @see UserRepo
     */
    @Bean
    public PrincipalExtractor githubPrincipalExtractor(UserRepo userRepo) {
        return map -> {
            String id = String.valueOf(map.get("id"));
            User user = userRepo.findById(id).orElseGet(() -> {
                User newUser = new User();
                newUser.setId(id);
                newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                newUser.getRoles().add(UserRoles.USER);
                newUser.setUsername((String) map.get("login"));
                newUser.setEmail(null);
                newUser.setUserPic((String) map.get("avatar_url"));
                return newUser;
            });
            return userRepo.save(user);
        };
    }

    /**
     * Compare all OAuth filters to one
     * @return Compared OAUth filters
     */
    @Bean
    public CompositeFilter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<OAuth2ClientAuthenticationProcessingFilter> filters = new ArrayList<>();
        ClientResources githubResource = githubDev();
        if(profile.equals("prod")) {
            githubResource = github();
        }
        filters.add(constructFilter(githubResource, "/login/github", githubPrincipalExtractor(userRepo)));
        filters.add(constructFilter(google(), "/login/google", googlePrincipalExtractor(userRepo)));
        filter.setFilters(filters);
        return filter;
    }

    /**
     * Construct new auth filter
     * @param client client resources
     * @param path login path
     * @param principalExtractor OAuth response to user extractor
     * @return Client authentication filter
     */
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
