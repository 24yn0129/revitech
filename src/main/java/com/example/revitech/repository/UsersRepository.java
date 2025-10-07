package com.example.revitech.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.revitech.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByName(String name);   // username → name に変更
    Optional<Users> findByEmail(String email); 
}

