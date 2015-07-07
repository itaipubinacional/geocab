package br.gov.itaipu.geocab.entity;

import android.util.Base64;

import java.io.Serializable;

/**
 * Created by Vinicius on 24/09/2014.
 */
public class User implements Serializable {

    public static final String FACEBOOK = "facebook";
    public static final String GOOGLEPLUS = "googleplus";
    public static final String GOOGLEPLUS_SCOPE = "https://www.googleapis.com/auth/userinfo.email";

    private static final String ACCESS_TOKEN = "access_token ";
    private static final String BASIC_TOKEN = "Basic ";

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
    private String credentials;
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

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public void setAccessTokenAuthorization(String username, String accessToken, String provider){

        final byte[] token = (username + ":" + accessToken + ":" + provider).getBytes();
        this.credentials = User.ACCESS_TOKEN + Base64.encodeToString(token, Base64.NO_WRAP);

    }

    public void setBasicAuthorization(String username, String password){

        final byte[] token = ( username + ":" + password ).getBytes();
        this.credentials = User.BASIC_TOKEN + Base64.encodeToString(token, Base64.NO_WRAP);

    }

    public static String createToken(String username, String password){

        final byte[] token = ( username + ":" + password ).getBytes();
        return Base64.encodeToString(token, Base64.NO_WRAP);

    }

}
