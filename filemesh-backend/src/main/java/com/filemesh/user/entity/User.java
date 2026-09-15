package com.filemesh.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="user", indexes = {
@Index(name = "fn_email", columnList="email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 40)
    private String firstName;

    @Column(length = 20)
    private String lastName;

    @Column(length = 60,nullable = false, unique = true)
    private String email;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private boolean registrationComplete;

    @PrePersist
    private void prePersist(){
    this.createdAt=LocalDateTime.now();
    this.registrationComplete=false;
}
}
