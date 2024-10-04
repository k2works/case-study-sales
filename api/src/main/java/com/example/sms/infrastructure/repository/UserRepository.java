package com.example.sms.infrastructure.repository;

import com.example.sms.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
