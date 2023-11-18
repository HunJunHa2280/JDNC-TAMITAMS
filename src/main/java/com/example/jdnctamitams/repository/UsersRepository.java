package com.example.jdnctamitams.repository;

import com.example.jdnctamitams.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository <Users, Long> {
    Optional<Users> findByName(String username);


}