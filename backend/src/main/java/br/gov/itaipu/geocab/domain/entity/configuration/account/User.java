package br.gov.itaipu.geocab.domain.entity.configuration.account;

import br.gov.itaipu.geocab.domain.entity.AbstractEntity;
import br.gov.itaipu.geocab.domain.entity.configuration.Configuration;
import br.gov.itaipu.geocab.domain.entity.configuration.preferences.BackgroundMap;
import br.gov.itaipu.geocab.domain.entity.configuration.preferences.Coordinates;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Vagner BC @since 02/06/2014 @version 1.0 @category
 */
@Entity
@JsonIgnoreProperties({"authorities"})
public class User extends AbstractEntity implements Serializable, UserDetails {
    /**
     *
     */
    private static final long serialVersionUID = -4052986759552589018L;

    // ----
    // Default user
    // ----

    public static final String NO_PASSWORD = new String("no password");

    public static final User ADMINISTRATOR = new User(1L, "Administrator",
            "admin@geocab.com.br", true, UserRole.ADMINISTRATOR, "admin");
    public static final User ANONYMOUS = new User(0L, "Anonymous", null, true,
            UserRole.ANONYMOUS, "anonymous");

	/*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    // Basic
    /**
     *
     */
    @NotEmpty
    @Column(nullable = false, length = 50)
    private String name;
    /**
     *
     */
    @Email
    @NotNull
    @Column(nullable = false, length = 144, unique = true)
    private String email;
    /**
     *
     */
    @NotNull
    @Column(nullable = false)
    private Boolean enabled;
    /**
     *
     */
    @NotBlank
    @Length(min = 8)
    @Column(nullable = false, length = 100)
    private String password;
    /**
     *
     */
    @Transient
    private String newPassword;
    /**
     *
     */
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    /**
     *
     */
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private BackgroundMap backgroundMap;

    /**
     *
     */
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Coordinates coordinates;

    /**
     *
     */
    @Transient
    private String token;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    public User() {
        this.getCoordinates();
        this.getBackgroundMap();
    }

    /**
     * @param id
     */
    public User(Long id) {
        super(id);
    }

    /**
     * @param name
     * @param email
     */
    public User(String name, String email) {
        this.email = email;
        this.name = name;
    }

    /**
     * @param id
     * @param name
     * @param email
     * @param enabled
     */
    public User(Long id, String name, String email, boolean enabled) {
        super(id);
        this.email = email;
        this.name = name;
        this.enabled = enabled;
    }

    /**
     * @param id
     */
    public User(Long id, String name, String email, boolean enabled,
                UserRole role) {
        super(id);
        this.email = email;
        this.name = name;
        this.enabled = enabled;
        this.role = role;
    }

    /**
     * @param id
     */
    public User(Long id, String name, String email, boolean enabled,
                UserRole role, String password) {
        super(id);
        this.email = email;
        this.name = name;
        this.enabled = enabled;
        this.password = password;
        this.role = role;
    }

    /**
     * @param name
     * @param email
     * @param enabled
     * @param password
     * @param newPassword
     * @param role
     * @param backgroundMap
     * @param coordinates
     */
    public User(String name, String email, Boolean enabled, String password,
                String newPassword, UserRole role, BackgroundMap backgroundMap,
                Coordinates coordinates) {
        super();
        this.name = name;
        this.email = email;
        this.enabled = enabled;
        this.password = password;
        this.newPassword = newPassword;
        this.role = role;
        this.backgroundMap = backgroundMap;
        this.coordinates = coordinates;
    }

    /**
     * @param name
     * @param email
     * @param enabled
     * @param password
     * @param newPassword
     * @param role
     * @param backgroundMap
     * @param coordinates
     * @param token
     */
    public User(String name, String email, Boolean enabled, String password,
                String newPassword, UserRole role, BackgroundMap backgroundMap,
                Coordinates coordinates, String token) {
        super();
        this.name = name;
        this.email = email;
        this.enabled = enabled;
        this.password = password;
        this.newPassword = newPassword;
        this.role = role;
        this.backgroundMap = backgroundMap;
        this.coordinates = coordinates;
        this.token = token;
    }

	/*-------------------------------------------------------------------
	 *							BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @return the enabled
     */
    public Boolean getEnabled() {
        if (enabled == null) {
            this.setEnabled(false);
        }
        return enabled;
    }

    /**
     *
     */
    @Override
    @Transient
    public Set<GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> authorities = new HashSet<>();

        if (role == null) {
            return null;
        }

        if (role.equals(UserRole.ADMINISTRATOR)) {
            authorities.add(UserRole.ADMINISTRATOR);
        }

        if (role.equals(UserRole.MODERATOR)) {
            authorities.add(UserRole.MODERATOR);
        }

        if (role.equals(UserRole.USER)) {
            authorities.add(UserRole.USER);
        }

        return authorities;
    }


    /**
     * @return the coordinates
     */
    public Coordinates getCoordinates() {
        if (coordinates == null) {
            coordinates = Coordinates.DEGREES_MINUTES_SECONDS;
        }
        return coordinates;
    }

    /**
     * @param coordinates the coordinates to set
     */
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            coordinates = Coordinates.DEGREES_MINUTES_SECONDS;
        }
        this.coordinates = coordinates;
    }

    /**
     *
     */
    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     *
     */
    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     *
     */
    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     *
     */
    @Override
    @Transient
    public boolean isEnabled() {
        return this.getEnabled();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.security.core.userdetails.UserDetails#getPassword()
     */
    @Override
    @Transient
    public String getPassword() {
        return this.password;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    @Override
    @Transient
    public String getUsername() {
        return this.email;
    }

    /**
     * @param configurations
     */
    public void verifyBackgroundMap(List<Configuration> configurations) {
        if (this.getBackgroundMap() == null) {
            if (configurations != null && configurations.size() > 0) {
                this.setBackgroundMap(configurations.get(0).getBackgroundMap());
            } else {
                this.setBackgroundMap(new Configuration().getBackgroundMap());
            }
        }
    }

    /**
     * @return the backgroundMap
     */
    public BackgroundMap getBackgroundMap() {
        if (backgroundMap == null) {
            backgroundMap = BackgroundMap.OPEN_STREET_MAP;
        }
        return backgroundMap;
    }

    /**
     * @param backgroundMap the backgroundMap to set
     */
    public void setBackgroundMap(BackgroundMap backgroundMap) {
        if (backgroundMap == null) {
            backgroundMap = BackgroundMap.OPEN_STREET_MAP;
        }
        this.backgroundMap = backgroundMap;
    }
	
	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * @param userRole the role to set
     */
    public void setRole(UserRole userRole) {
        this.role = userRole;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

}