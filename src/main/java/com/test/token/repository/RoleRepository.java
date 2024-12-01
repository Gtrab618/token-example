package com.test.token.repository;

import com.test.token.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  RoleRepository extends JpaRepository<RoleEntity,Long> {

}
