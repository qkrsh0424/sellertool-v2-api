package com.sellertool.server.domain.refresh_token.model.repository;

import java.util.Optional;
import java.util.UUID;

import com.sellertool.server.domain.refresh_token.model.entity.RefreshTokenEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {

    Optional<RefreshTokenEntity> findById(UUID id);
}
