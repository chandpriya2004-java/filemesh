package com.filemesh.user.repo;

import com.filemesh.user.entity.OtpValidation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpValidationRepo extends JpaRepository<OtpValidation ,String> {

    OtpValidation findByEmail(String Email);

    OtpValidation findTopByEmailOrderByCreatedAtDesc(String email);
}
