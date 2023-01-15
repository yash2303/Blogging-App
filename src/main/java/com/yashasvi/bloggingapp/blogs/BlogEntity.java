package com.yashasvi.bloggingapp.blogs;


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
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Entity(name = "blogs")
public class BlogEntity extends BaseEntity<Long> {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity author;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToMany
    List<UserEntity> likedBy;
}
