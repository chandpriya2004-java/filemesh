package com.filemesh.storage.entity;

import com.filemesh.user.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_store", indexes = {
        @Index(name = "fn_user_id", columnList="user_id"),
        @Index(name = "fn_storage_key", columnList="storage_key")
})
public class UserStore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String original_name;

    @Column(nullable = false, unique = true)
    private String storage_key;

    private String content_type;

    private Long size;

    @Column(nullable = false)
    private String user_id;

}
