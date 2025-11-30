package com.example.blog.post;

import com.example.blog.dto.PostDetailResponse;
import com.example.blog.dto.PostRequest;
import com.example.blog.dto.PostSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/api/admin/posts")
@Tag(name = "Admin Posts")
@SecurityRequirement(name = "bearerAuth")
public class PostAdminController {

    private static final Logger log = LoggerFactory.getLogger(PostAdminController.class);
    private final PostService postService;

    public PostAdminController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "List posts (admin)")
    public Page<PostSummaryResponse> list(@Parameter(description = "Filter by post status") @RequestParam(value = "status", required = false) PostStatus status,
                                          @Parameter(description = "Search by title/body") @RequestParam(value = "q", required = false) String query,
                                          @ParameterObject @PageableDefault Pageable pageable) {
        log.info("GET /api/admin/posts status={} q='{}' page={} size={}", status, query, pageable.getPageNumber(), pageable.getPageSize());
        return postService.listAdmin(status, query, pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a post by id (admin)")
    public PostDetailResponse get(@PathVariable Long id) {
        log.info("GET /api/admin/posts/{}", id);
        return postService.getAdmin(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a post")
    public ResponseEntity<PostDetailResponse> create(@Valid @RequestBody PostRequest request) {
        log.info("POST /api/admin/posts title='{}' status={}", request.getTitle(), request.getStatus());
        return ResponseEntity.ok(postService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a post")
    public PostDetailResponse update(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
        log.info("PUT /api/admin/posts/{} title='{}' status={}", id, request.getTitle(), request.getStatus());
        return postService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/admin/posts/{}", id);
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish a post")
    public PostDetailResponse publish(@PathVariable Long id) {
        log.info("POST /api/admin/posts/{}/publish", id);
        return postService.publish(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/unpublish")
    @Operation(summary = "Unpublish a post")
    public PostDetailResponse unpublish(@PathVariable Long id) {
        log.info("POST /api/admin/posts/{}/unpublish", id);
        return postService.unpublish(id);
    }
}
