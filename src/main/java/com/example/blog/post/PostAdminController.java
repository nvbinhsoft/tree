package com.example.blog.post;

import com.example.blog.dto.PostDetailResponse;
import com.example.blog.dto.PostRequest;
import com.example.blog.dto.PostSummaryResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/posts")
public class PostAdminController {

    private final PostService postService;

    public PostAdminController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<PostSummaryResponse> list(@RequestParam(value = "status", required = false) PostStatus status,
                                          @RequestParam(value = "q", required = false) String query,
                                          @PageableDefault Pageable pageable) {
        return postService.listAdmin(status, query, pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public PostDetailResponse get(@PathVariable Long id) {
        return postService.getAdmin(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDetailResponse> create(@Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public PostDetailResponse update(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
        return postService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/publish")
    public PostDetailResponse publish(@PathVariable Long id) {
        return postService.publish(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/unpublish")
    public PostDetailResponse unpublish(@PathVariable Long id) {
        return postService.unpublish(id);
    }
}
