package com.filemesh.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Builder
@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "otp_auth", indexes = {@Index(name = "fn_email", columnList = "email"), @Index(name = "fn_otp", columnList = "otp"),
        @Index(name = "fn_expiryAt", columnList = "expiryAt") })
public class OtpValidation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "UUID")
    private String id;
    @Column(length = 60, nullable = false)
    private String email;
    @Column(length = 6, nullable = false)
    private String otp;
    private boolean verified;
    private Integer optGenerated;
    private LocalDateTime createdAt;
    private LocalDateTime expiryAt;
    private int otpSentCount;

    @PrePersist
    private void prePersist() {
        this.verified = false;
    }
}
