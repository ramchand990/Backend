package com.healspan.claimservice.mstupload.security;

import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserMstDetails implements UserDetails {

    private Logger logger = LoggerFactory.getLogger(UserMstDetails.class);

    private String userName;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;

    public UserMstDetails(UserMst user) {
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.authorities = new ArrayList<>();
    }
    public UserMstDetails() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
