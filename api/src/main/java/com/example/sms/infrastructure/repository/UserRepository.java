package com.example.sms.infrastructure.repository;

import com.example.sms.infrastructure.datasource.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserJpaEntity, String> {
}
