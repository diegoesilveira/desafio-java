package com.khipo.desafiojava.repository;

import com.khipo.desafiojava.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserDetails> findByLogin(String login);
}
