package hackaton.fastdisision.config.web;

import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.3
 **/
@EnableJdbcHttpSession
public class Session extends AbstractHttpSessionApplicationInitializer {

}
