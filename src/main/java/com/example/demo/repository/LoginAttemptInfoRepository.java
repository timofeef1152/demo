package com.example.demo.repository;

import com.example.demo.model.entity.LoginAttemptInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoginAttemptInfoRepository extends JpaRepository<LoginAttemptInfo, UUID> {
}
