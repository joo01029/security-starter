package com.example.securitytest.domain.repo;

import com.example.securitytest.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
	Optional<User> findById(String id);
	Optional<User> findByIdx(Long idx);
}
