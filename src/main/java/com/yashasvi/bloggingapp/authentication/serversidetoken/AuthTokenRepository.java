package com.yashasvi.bloggingapp.authentication.serversidetoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity, UUID> {
}