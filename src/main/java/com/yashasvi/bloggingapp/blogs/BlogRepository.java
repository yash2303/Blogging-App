package com.yashasvi.bloggingapp.blogs;

import com.yashasvi.bloggingapp.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
    List<BlogEntity> findAllByAuthor(UserEntity author);
}