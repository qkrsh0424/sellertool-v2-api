package com.sellertool.server.domain.user.model.repository;

import java.util.Optional;

import com.sellertool.server.domain.user.model.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
    
    UserEntity findByEmailAndPassword(String email, String password);

    Optional<UserEntity> findByEmail(String email);
}
