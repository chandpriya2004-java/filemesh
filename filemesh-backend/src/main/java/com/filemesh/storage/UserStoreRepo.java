package com.filemesh.storage;

import com.filemesh.storage.entity.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoreRepo extends JpaRepository<UserStore, String> {

}
