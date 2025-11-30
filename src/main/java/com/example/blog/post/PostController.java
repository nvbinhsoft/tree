package com.example.blog.post;

import com.example.blog.dto.PostDetailResponse;
import com.example.blog.dto.PostSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springdoc.core.annotations.ParameterObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Public Posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "List published posts", description = "Returns a paginated list of published posts.")
    public Page<PostSummaryResponse> list(@Parameter(description = "Full-text search query", name = "q")
                                          @RequestParam(value = "q", required = false) String query,
                                          @ParameterObject @PageableDefault Pageable pageable) {
        log.info("GET /api/posts q='{}' page={} size={}", query, pageable.getPageNumber(), pageable.getPageSize());
        return postService.listPublished(query, pageable);
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get a published post by slug")
    public PostDetailResponse getBySlug(@PathVariable String slug) {
        log.info("GET /api/posts/{}", slug);
        return postService.getBySlug(slug);
    }
}
