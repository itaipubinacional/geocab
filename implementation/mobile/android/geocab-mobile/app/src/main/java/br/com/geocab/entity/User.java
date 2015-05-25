package br.com.geocab.entity;

import java.io.Serializable;

/**
 * Created by Vinicius on 24/09/2014.
 */
public class User implements Serializable {

    /**
     * Id {@link User}
     */
    private Long id;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private String email;
    /**
     *
     */
    private Boolean enabled;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private UserRole role;

    public User() {
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User(String password)
    {
        this.password = password;
    }

    public User(String name, String email, Boolean enabled, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.enabled = enabled;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
