package com.nm.novamart.Repository;

import com.nm.novamart.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User getUserByUserName(String username);
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
}
