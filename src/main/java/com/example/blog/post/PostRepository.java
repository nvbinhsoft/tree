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

    @Query(value = "select * from posts p where (cast(:#{#status?.name()} as varchar) is null or p.status = cast(:#{#status?.name()} as varchar)) and (cast(:query as varchar) is null or lower(p.title) like lower(concat('%', cast(:query as varchar), '%')) or lower(p.body) like lower(concat('%', cast(:query as varchar), '%'))) order by p.updated_at desc", countQuery = "select count(*) from posts p where (cast(:#{#status?.name()} as varchar) is null or p.status = cast(:#{#status?.name()} as varchar)) and (cast(:query as varchar) is null or lower(p.title) like lower(concat('%', cast(:query as varchar), '%')) or lower(p.body) like lower(concat('%', cast(:query as varchar), '%')))", nativeQuery = true)
    Page<Post> searchAdmin(@Param("status") PostStatus status, @Param("query") String query, Pageable pageable);

    @Query(value = "select * from posts p where p.status = 'PUBLISHED' and (cast(:query as varchar) is null or lower(p.title) like lower(concat('%', cast(:query as varchar), '%')) or lower(p.body) like lower(concat('%', cast(:query as varchar), '%'))) order by p.published_at desc", countQuery = "select count(*) from posts p where p.status = 'PUBLISHED' and (cast(:query as varchar) is null or lower(p.title) like lower(concat('%', cast(:query as varchar), '%')) or lower(p.body) like lower(concat('%', cast(:query as varchar), '%')))", nativeQuery = true)
    Page<Post> searchPublished(@Param("query") String query, Pageable pageable);
}
