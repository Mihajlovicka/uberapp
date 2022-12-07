package com.example.demo.repository;

import com.example.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(nativeQuery = true, value="select * from roles u where u.name = :#{#roleName}")
    List<Role> findByName(@Param("roleName") String roleName);
}
