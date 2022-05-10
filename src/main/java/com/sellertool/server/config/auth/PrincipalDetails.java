package com.sellertool.server.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import com.sellertool.server.domain.user.entity.UserEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails {
    
    private UserEntity user;
//    private Collection<? extends GrantedAuthority> authorities;

    public PrincipalDetails(UserEntity user){
        this.user = user;
    }

    // 권한 설정
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(() -> user.getRoles());
        user.getRoleList().forEach(r->{
            authorities.add(()->r);
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return user.getPassword() == null ? "" : user.getPassword();
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return user.getUsername() == null ? "" : user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    // false -> true
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    public String getSalt(){
        return this.user.getSalt();
    }
    
}
