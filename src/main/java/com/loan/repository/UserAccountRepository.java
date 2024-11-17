package com.loan.repository;

import com.loan.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

        @Query(value = "SELECT * FROM user_account WHERE username = :username", nativeQuery = true)
        Optional<UserAccount> findByUsername(@Param("username") String username);

        @Query(value = "SELECT COUNT(*) > 0 FROM user_account WHERE username = :username",  nativeQuery = true)
        boolean existsByUsername(@Param("username") String username);

        @Query(value = "SELECT * FROM user_account WHERE id = :id", nativeQuery = true)
        Optional<UserAccount> findById(@Param("id") String id);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO user_account (id, username, password, role_id) VALUES (?, ?, ?, ?)", nativeQuery = true)
        void save(String id, String username, String password,  String role);
}
