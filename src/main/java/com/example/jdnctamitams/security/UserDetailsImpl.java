package com.example.jdnctamitams.security;


import com.example.jdnctamitams.entity.UserRoleEnum;
import com.example.jdnctamitams.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
// 일반적으로 UserDetailsImpl 클래스는 다음과 같은 목적으로 사용됩니다:
// 사용자 정보 저장: 사용자의 인증 정보(예: 사용자명, 비밀번호)와 권한 정보(예: 역할)를 저장합니다.
//사용자 정보 제공: 스프링 시큐리티에게 사용자의 정보를 제공하여 인증 및 권한 부여를 지원합니다.
//커스터마이징: 필요한 경우 커스텀 로직을 추가하여 사용자 세부 정보를 관리합니다. 예를 들어, 사용자 역할과 권한 정보를 가져오는 로직을 정의할 수 있습니다.
//사용자의 상태 제어: 사용자 계정의 상태(만료 여부, 비밀번호 만료 여부, 계정 잠금 여부)를 설정할 수 있습니다.


import java.util.ArrayList;


public class UserDetailsImpl implements UserDetails {

    private final Users users;

    public UserDetailsImpl(Users users) {
        this.users = users;
    }


    public Users getUser() {
        return users;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = users.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    } // 역할을 가져온다는 의미다 그렇게 이해하자 대충 그렇게 아래꺼 사용하게 한다 이건 사용하려면 가져와라

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
        return true;
    }
}