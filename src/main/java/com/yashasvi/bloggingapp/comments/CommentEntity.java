package com.yashasvi.bloggingapp.comments;

import com.yashasvi.bloggingapp.blogs.BlogEntity;
import com.yashasvi.bloggingapp.common.BaseEntity;
import com.yashasvi.bloggingapp.users.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Entity(name = "comments")
public class CommentEntity extends BaseEntity<Long> {
    @JoinColumn(columnDefinition = "TEXT", nullable = false)
    @ManyToOne
    private BlogEntity blog;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity commenter;

    @ManyToMany
    List<UserEntity> likedBy;
}
