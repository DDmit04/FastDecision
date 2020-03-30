package hackaton.fastdisision.data;

import org.springframework.security.core.GrantedAuthority;

/**
 * Represents user roles/authorities
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
public enum UserRoles implements GrantedAuthority {

    USER, ADMIN;

	@Override
	public String getAuthority() {
		return name();
	}


}