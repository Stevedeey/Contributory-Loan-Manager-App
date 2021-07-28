package com.example.contributoryloanapp.service;

import com.example.contributoryloanapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService extends JpaRepository<User, Long> {
}
