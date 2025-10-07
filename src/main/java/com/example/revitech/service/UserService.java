package com.example.revitech.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.revitech.entity.Users;
import com.example.revitech.form.SignupForm;
import com.example.revitech.repository.UsersRepository;

@Service
public class UserService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Users registerUser(SignupForm form) {
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            throw new IllegalArgumentException("パスワードが一致しません");
        }

        // 役割制御
        String role = form.getRole().toUpperCase();
        if (!role.equals("USER") && !role.equals("TEACHER") && !role.equals("ADMIN")) {
            role = "USER";
        }

        Users user = new Users();
        user.setName(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(role);
        user.setStatus("active");

        return usersRepository.save(user);
    }

    public boolean isUsernameTaken(String username) {
        return usersRepository.findByName(username).isPresent();
    }

    public boolean isEmailTaken(String email) {
        return usersRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getName())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
