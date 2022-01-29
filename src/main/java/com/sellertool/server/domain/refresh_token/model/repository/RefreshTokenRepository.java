package com.sellertool.server.domain.refresh_token.model.repository;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.sellertool.server.domain.refresh_token.model.entity.RefreshTokenEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {

    Optional<RefreshTokenEntity> findById(UUID id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM refresh_token rt WHERE rt.updated_at < (SELECT MIN(temp.updated_at)\n"
        + "FROM (SELECT rt2.updated_at FROM refresh_token rt2\n"
        + "WHERE rt2.user_id=:userId ORDER BY rt2.updated_at DESC LIMIT 2) AS temp)"
    , nativeQuery = true)
    public void deleteOldRefreshTokenForUser(String userId);
}
