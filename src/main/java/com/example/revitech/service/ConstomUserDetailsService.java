package com.example.revitech.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.revitech.entity.Users;
import com.example.revitech.repository.UsersRepository;

@Service
public class ConstomUserDetailsService implements UserDetailsService {

    private final UsersRepository userRepository;

    public  ConstomUserDetailsService(UsersRepository repo) {
        this.userRepository = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getName())
                .password(user.getPassword())
                .roles(user.getRole().toUpperCase())
                .build();
    }
}
