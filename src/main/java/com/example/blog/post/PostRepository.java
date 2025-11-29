package com.example.blog.post;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);

    @Query("select p from Post p where p.status = com.example.blog.post.PostStatus.PUBLISHED order by p.publishedAt desc")
    Page<Post> findPublished(Pageable pageable);

    @Query("select p from Post p where (:status is null or p.status = :status) and (:query is null or lower(p.title) like lower(concat('%', :query, '%')) or lower(p.body) like lower(concat('%', :query, '%'))) order by p.updatedAt desc")
    Page<Post> searchAdmin(@Param("status") PostStatus status, @Param("query") String query, Pageable pageable);

    @Query("select p from Post p where p.status = com.example.blog.post.PostStatus.PUBLISHED and (:query is null or lower(p.title) like lower(concat('%', :query, '%')) or lower(p.body) like lower(concat('%', :query, '%'))) order by p.publishedAt desc")
    Page<Post> searchPublished(@Param("query") String query, Pageable pageable);
}
