package com.example.blog.post;

import com.example.blog.common.SlugUtil;
import com.example.blog.dto.PostDetailResponse;
import com.example.blog.dto.PostRequest;
import com.example.blog.dto.PostSummaryResponse;
import com.example.blog.dto.TagResponse;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public Page<PostSummaryResponse> listPublished(String query, Pageable pageable) {
        return postRepository.searchPublished(query, pageable).map(this::toSummaryDto);
    }

    public PostDetailResponse getBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .filter(p -> p.getStatus() == PostStatus.PUBLISHED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return toDetailDto(post);
    }

    public Page<PostSummaryResponse> listAdmin(PostStatus status, String query, Pageable pageable) {
        return postRepository.searchAdmin(status, query, pageable).map(this::toDetailDto);
    }

    public PostDetailResponse getAdmin(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return toDetailDto(post);
    }

    @Transactional
    public PostDetailResponse create(PostRequest request) {
        Post post = new Post();
        applyRequest(post, request);
        postRepository.save(post);
        return toDetailDto(post);
    }

    @Transactional
    public PostDetailResponse update(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        applyRequest(post, request);
        postRepository.save(post);
        return toDetailDto(post);
    }

    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public PostDetailResponse publish(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        post.setStatus(PostStatus.PUBLISHED);
        if (post.getPublishedAt() == null) {
            post.setPublishedAt(OffsetDateTime.now());
        }
        postRepository.save(post);
        return toDetailDto(post);
    }

    @Transactional
    public PostDetailResponse unpublish(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        post.setStatus(PostStatus.DRAFT);
        postRepository.save(post);
        return toDetailDto(post);
    }

    private void applyRequest(Post post, PostRequest request) {
        post.setTitle(request.getTitle());
        post.setExcerpt(request.getExcerpt());
        post.setBody(request.getBody());
        post.setCoverImageUrl(request.getCoverImageUrl());
        post.setStatus(request.getStatus());

        if (post.getSlug() == null || request.getSlug() != null) {
            String baseSlug = request.getSlug() != null ? request.getSlug() : SlugUtil.toSlug(request.getTitle());
            post.setSlug(generateUniqueSlug(baseSlug, post.getId()));
        }

        if (request.getStatus() == PostStatus.PUBLISHED && post.getPublishedAt() == null) {
            post.setPublishedAt(OffsetDateTime.now());
        }

        post.setReadTimeMinutes(calculateReadTime(request.getBody()));

        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null) {
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag not found"));
                tags.add(tag);
            }
        }
        post.setTags(tags);
    }

    private int calculateReadTime(String body) {
        String[] words = body.trim().split("\\s+");
        int wordCount = words.length;
        return Math.max(1, (int) Math.ceil(wordCount / 230.0));
    }

    private String generateUniqueSlug(String base, Long currentId) {
        String candidate = base;
        int counter = 1;
        while (true) {
            String finalCandidate = candidate;
            boolean exists = postRepository.findBySlug(finalCandidate)
                    .filter(p -> currentId == null || !p.getId().equals(currentId))
                    .isPresent();
            if (!exists) {
                return finalCandidate;
            }
            counter++;
            candidate = base + "-" + counter;
        }
    }

    private PostSummaryResponse toSummaryDto(Post post) {
        List<TagResponse> tags = post.getTags().stream()
                .map(t -> new TagResponse(t.getId(), t.getName(), t.getSlug()))
                .toList();
        return new PostSummaryResponse(post.getId(), post.getSlug(), post.getTitle(), post.getExcerpt(),
                post.getPublishedAt(), post.getCoverImageUrl(), post.getReadTimeMinutes(), tags);
    }

    private PostDetailResponse toDetailDto(Post post) {
        List<TagResponse> tags = post.getTags().stream()
                .map(t -> new TagResponse(t.getId(), t.getName(), t.getSlug()))
                .toList();
        return new PostDetailResponse(post.getId(), post.getSlug(), post.getTitle(), post.getExcerpt(),
                post.getPublishedAt(), post.getCoverImageUrl(), post.getReadTimeMinutes(), tags,
                post.getBody(), post.getStatus());
    }
}
