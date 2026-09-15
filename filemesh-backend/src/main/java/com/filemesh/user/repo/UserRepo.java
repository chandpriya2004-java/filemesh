package com.filemesh.user.repo;

import com.filemesh.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,String> {

    User findByEmail(String email);

    boolean existsById(String userId);
}
