package hackaton.fastdisision.data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hackaton.fastdisision.views.VotingView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents user entity
 * @author Dmitrochenkov Daniil
 * @version 1.3
 */
@Getter
@Setter
@Entity
@Table(name = "usr",
        indexes = {
                @Index(columnList = "username", name = "username_index"),
                @Index(columnList = "email", name = "email_index"),
                @Index(columnList = "registrationDate", name = "registration_date_index")
        })
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "fullUserData",
                attributeNodes = {
                        @NamedAttributeNode("roles"),
                        @NamedAttributeNode("userVotings"),
                }
        ),
        @NamedEntityGraph(
                name = "userDataWithRoles",
                attributeNodes = {
                        @NamedAttributeNode("roles"),
                }
        ),
        @NamedEntityGraph(
                name = "userDataWithVotings",
                attributeNodes = {
                        @NamedAttributeNode("userVotings"),
                }
        ),
})
public class User implements UserDetails {

    @Id
    @JsonView(VotingView.Id.class)
    private String id;

    @JsonView(VotingView.ChartData.class)
    @NotBlank(message = "username can not be empty")
    private String username;

    @JsonView(VotingView.FullData.class)
    private String email;

    @JsonView(VotingView.FullData.class)
    @NotBlank(message = "password can not be empty")
    private String password;

    @JsonView(VotingView.FullData.class)
    private LocalDateTime registrationDate;

    @JsonView(VotingView.MinimalData.class)
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>(Collections.singletonList(UserRole.USER));

    @JsonView(VotingView.CoreData.class)
    private String userPic;

    @JsonView(VotingView.FullData.class)
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
    private Set<Voting> userVotings = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getId() != null ? getId().equals(user.getId()) : user.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name())));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    /**
     * Check is user has admin permissions
     *
     * @return is user has admin permissions
     */
    public boolean isAdmin() {
        return roles.contains(UserRole.ADMIN);
    }

    /**
     * Remove voting from user votings
     *
     * @param voting voting to remove
     */
    public void deleteVotingRelationship(Voting voting) {
        userVotings.remove(voting);
    }
}