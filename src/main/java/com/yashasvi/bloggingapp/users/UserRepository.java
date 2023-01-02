package com.yashasvi.bloggingapp.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity getByUsername(String userName);

    UserEntity getByEmail(String email);
}
