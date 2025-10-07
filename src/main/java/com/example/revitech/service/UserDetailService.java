package com.example.revitech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.revitech.entity.Users;
import com.example.revitech.repository.UsersRepository;

@Service
public class UserDetailService implements UserDetailsService {

    private final UsersRepository teacherRepository;

    @Autowired
    public UserDetailService(UsersRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Users user = teacherRepository.findByName(username)
    		    .orElseThrow(() -> new UsernameNotFoundException("ユーザーが存在しません: " + username));


        return User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles(user.getRole()) // 例: USER
                .build();
    }
}
