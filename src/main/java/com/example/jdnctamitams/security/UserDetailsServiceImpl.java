package com.example.jdnctamitams.security;

import com.example.jdnctamitams.entity.Users;
import com.example.jdnctamitams.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Users users = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + name));

        return new UserDetailsImpl(users);
    }
}