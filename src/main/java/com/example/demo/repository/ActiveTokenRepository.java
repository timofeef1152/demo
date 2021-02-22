package com.example.demo.repository;

import com.example.demo.model.entity.ActiveToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActiveTokenRepository extends JpaRepository<ActiveToken, UUID> {
    @Query("select t from ActiveToken t where t.token = :token and t.ipAddress = :ipAddress")
    Optional<ActiveToken> findByTokenAndIpAddress(@Param("token") String token, @Param("ipAddress") String ipAddress);

    @Transactional
    @Modifying
    @Query("delete ActiveToken t where t.token = :token")
    void deleteByToken(@Param("token") String token);
}
