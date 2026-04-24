package com.ufrn.pertindetu.base.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link UserDetails} representing user information for authentication.
 */
public class UserDetailsInfo implements UserDetails {

    private String id;

    private String name;

    private String email;

    /**
     * Default constructor.
     */
    public UserDetailsInfo() {
    }

    /**
     * Constructs a user details instance with the given ID, name, and email.
     *
     * @param id    the user's unique identifier
     * @param name  the user's full name
     * @param email the user's email address
     */
    public UserDetailsInfo(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the user's full name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's full name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user's ID.
     *
     * @return the ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the user's ID.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the user's email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's login.
     *
     * @param login the email to set
     */
    public void setEmail(String login) {
        this.email = login;
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(null));
    }

    /**
     * Returns the user's password (empty in this implementation).
     *
     * @return an empty string
     */
    @Override
    public String getPassword() {
        return "";
    }

    /**
     * Returns the username used for authentication (email in this case).
     *
     * @return the email
     */
    @Override
    public String getUsername() {
        return this.name;
    }

    /**
     * Indicates whether the account has expired.
     *
     * @return false
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * Indicates whether the account is locked.
     *
     * @return false
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * Indicates whether the credentials have expired.
     *
     * @return false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * Indicates whether the account is enabled.
     *
     * @return false
     */
    @Override
    public boolean isEnabled() {
        return false;
    }

}

