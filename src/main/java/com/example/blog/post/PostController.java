package com.example.blog.post;

import com.example.blog.dto.PostDetailResponse;
import com.example.blog.dto.PostSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Page<PostSummaryResponse> list(@RequestParam(value = "q", required = false) String query,
                                          @PageableDefault Pageable pageable) {
        return postService.listPublished(query, pageable);
    }

    @GetMapping("/{slug}")
    public PostDetailResponse getBySlug(@PathVariable String slug) {
        return postService.getBySlug(slug);
    }
}
