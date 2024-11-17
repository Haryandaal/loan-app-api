package com.loan.repository;

import com.loan.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, String> {

    Optional<UserRole> findByName(String name);

    boolean existsByName(String name);
}
