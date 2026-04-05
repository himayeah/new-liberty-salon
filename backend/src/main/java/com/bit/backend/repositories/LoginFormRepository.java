package com.bit.backend.repositories;

import com.bit.backend.entities.LoginFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//explained: <LoginFormEntity, Long> = The name you represented the DB table as and the primary key (Id) type (Long)
public interface LoginFormRepository extends JpaRepository<LoginFormEntity, Long> {
}
