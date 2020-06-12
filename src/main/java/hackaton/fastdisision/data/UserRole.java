package hackaton.fastdisision.data;

import org.springframework.security.core.GrantedAuthority;

/**
 * Represents user roles/authorities
 * @author Dmitrochenkov Daniil
 * @version 1.1
 */
public enum UserRole implements GrantedAuthority {

    USER, ADMIN;

	@Override
	public String getAuthority() {
		return name();
	}


}