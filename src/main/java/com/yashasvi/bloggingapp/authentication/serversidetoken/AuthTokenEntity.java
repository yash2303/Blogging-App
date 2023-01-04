package com.yashasvi.bloggingapp.authentication.serversidetoken;

import com.yashasvi.bloggingapp.users.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "auth_tokens")
public class AuthTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID token;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    Date expirationTimestamp;
}
