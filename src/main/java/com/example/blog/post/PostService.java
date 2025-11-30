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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class PostService {
    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public Page<PostSummaryResponse> listPublished(String query, Pageable pageable) {
        log.debug("Listing published posts: query='{}', page={}, size={}", query, pageable.getPageNumber(), pageable.getPageSize());
        return postRepository.searchPublished(query, pageable).map(this::toSummaryDto);
    }

    public PostDetailResponse getBySlug(String slug) {
        log.debug("Fetching published post by slug={}", slug);
        Post post = postRepository.findBySlug(slug)
                .filter(p -> p.getStatus() == PostStatus.PUBLISHED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return toDetailDto(post);
    }

    public Page<PostSummaryResponse> listAdmin(PostStatus status, String query, Pageable pageable) {
        log.debug("Listing admin posts: status={}, query='{}', page={}, size={}", status, query, pageable.getPageNumber(), pageable.getPageSize());
        return postRepository.searchAdmin(status, query, pageable).map(this::toDetailDto);
    }

    public PostDetailResponse getAdmin(Long id) {
        log.debug("Fetching admin post id={}", id);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return toDetailDto(post);
    }

    @Transactional
    public PostDetailResponse create(PostRequest request) {
        log.info("Creating post title='{}', status={}", request.getTitle(), request.getStatus());
        Post post = new Post();
        applyRequest(post, request);
        postRepository.save(post);
        log.info("Created post id={}, slug={}", post.getId(), post.getSlug());
        return toDetailDto(post);
    }

    @Transactional
    public PostDetailResponse update(Long id, PostRequest request) {
        log.info("Updating post id={}, title='{}', status={}", id, request.getTitle(), request.getStatus());
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        applyRequest(post, request);
        postRepository.save(post);
        log.info("Updated post id={}, slug={}", post.getId(), post.getSlug());
        return toDetailDto(post);
    }

    @Transactional
    public void delete(Long id) {
        log.warn("Deleting post id={}", id);
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public PostDetailResponse publish(Long id) {
        log.info("Publishing post id={}", id);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        post.setStatus(PostStatus.PUBLISHED);
        if (post.getPublishedAt() == null) {
            post.setPublishedAt(OffsetDateTime.now());
        }
        postRepository.save(post);
        log.info("Published post id={}, publishedAt={}", post.getId(), post.getPublishedAt());
        return toDetailDto(post);
    }

    @Transactional
    public PostDetailResponse unpublish(Long id) {
        log.info("Unpublishing post id={}", id);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        post.setStatus(PostStatus.DRAFT);
        postRepository.save(post);
        log.info("Unpublished post id={}", post.getId());
        return toDetailDto(post);
    }

    private void applyRequest(Post post, PostRequest request) {
        log.debug("Applying request for post id={}, incoming slug={}, tagIds={}", post.getId(), request.getSlug(), request.getTagIds());
        post.setTitle(request.getTitle());
        post.setExcerpt(request.getExcerpt());
        post.setBody(request.getBody());
        post.setCoverImageUrl(request.getCoverImageUrl());
        post.setStatus(request.getStatus());

        if (post.getSlug() == null || request.getSlug() != null) {
            String baseSlug = request.getSlug() != null ? request.getSlug() : SlugUtil.toSlug(request.getTitle());
            post.setSlug(generateUniqueSlug(baseSlug, post.getId()));
            log.debug("Generated slug for post id={}: {}", post.getId(), post.getSlug());
        }

        if (request.getStatus() == PostStatus.PUBLISHED && post.getPublishedAt() == null) {
            post.setPublishedAt(OffsetDateTime.now());
            log.debug("Set publishedAt={} for post id={}", post.getPublishedAt(), post.getId());
        }

        post.setReadTimeMinutes(calculateReadTime(request.getBody()));
        log.debug("Calculated readTimeMinutes={} for post id={}", post.getReadTimeMinutes(), post.getId());

        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null) {
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag not found"));
                tags.add(tag);
            }
        }
        post.setTags(tags);
        log.debug("Attached {} tags to post id={}", tags.size(), post.getId());
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
