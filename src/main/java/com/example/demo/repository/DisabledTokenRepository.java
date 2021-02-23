package com.example.demo.repository;

import com.example.demo.model.entity.DisabledToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DisabledTokenRepository extends JpaRepository<DisabledToken, UUID> {
    @Query("select t from DisabledToken t where t.token = :token and t.ipAddress = :ipAddress")
    Optional<DisabledToken> findByTokenAndIpAddress(@Param("token") String token, @Param("ipAddress") String ipAddress);
}
