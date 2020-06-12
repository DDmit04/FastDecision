package hackaton.fastdisision.config.web;

import hackaton.fastdisision.config.auth.CustomUserInfoTokenServices;
import hackaton.fastdisision.data.ClientResources;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CompositeFilter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 **/
@Component
public class OAuthFilter extends CompositeFilter {

    @Value("${voting.public.key}")
    private String publicVotingKey;

    private final OAuth2ClientContext oauth2ClientContext;
    private final ClientResources githubOAuthResource;
    private final ClientResources googleOAuthResource;

    private final PrincipalExtractor googleUserPrincipalExtractor;
    private final PrincipalExtractor githubUserPrincipalExtractor;

    public OAuthFilter(@Qualifier("oauth2ClientContext")OAuth2ClientContext oauth2ClientContext, ClientResources githubOAuthResource, ClientResources googleOAuthResource, PrincipalExtractor googleUserPrincipalExtractor, PrincipalExtractor githubUserPrincipalExtractor) {
        this.oauth2ClientContext = oauth2ClientContext;
        this.githubOAuthResource = githubOAuthResource;
        this.googleOAuthResource = googleOAuthResource;
        this.googleUserPrincipalExtractor = googleUserPrincipalExtractor;
        this.githubUserPrincipalExtractor = githubUserPrincipalExtractor;
    }

    /**
     * Setup filter after props set
     */
    @PostConstruct
    protected void setupFilters() {
        List<OAuth2ClientAuthenticationProcessingFilter> filters = new ArrayList<>();
        filters.add(constructFilter(githubOAuthResource, "/login/github", githubUserPrincipalExtractor));
        filters.add(constructFilter(googleOAuthResource, "/login/google", googleUserPrincipalExtractor));
        this.setFilters(filters);
    }

    /**
     * Construct new auth filter
     *
     * @param client client resources
     * @param path login path
     * @param principalExtractor OAuth response to user extractor
     * @return Client authentication filter
     */
    protected OAuth2ClientAuthenticationProcessingFilter constructFilter(ClientResources client, String path, PrincipalExtractor principalExtractor) {
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
