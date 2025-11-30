package com.example.blog.post;

import com.example.blog.common.SlugUtil;
import com.example.blog.dto.TagRequest;
import com.example.blog.dto.TagResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class TagService {
    private static final Logger log = LoggerFactory.getLogger(TagService.class);
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagResponse> listAll() {
        log.debug("Listing all tags");
        return tagRepository.findAll().stream()
                .map(t -> new TagResponse(t.getId(), t.getName(), t.getSlug()))
                .toList();
    }

    @Transactional
    public TagResponse create(TagRequest request) {
        log.info("Creating tag name='{}'", request.getName());
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setSlug(generateUniqueSlug(SlugUtil.toSlug(request.getName()), null));
        tagRepository.save(tag);
        log.info("Created tag id={}, slug={}", tag.getId(), tag.getSlug());
        return new TagResponse(tag.getId(), tag.getName(), tag.getSlug());
    }

    @Transactional
    public TagResponse update(Long id, TagRequest request) {
        log.info("Updating tag id={}, name='{}'", id, request.getName());
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        tag.setName(request.getName());
        tag.setSlug(generateUniqueSlug(SlugUtil.toSlug(request.getName()), id));
        tagRepository.save(tag);
        log.info("Updated tag id={}, slug={}", tag.getId(), tag.getSlug());
        return new TagResponse(tag.getId(), tag.getName(), tag.getSlug());
    }

    @Transactional
    public void delete(Long id) {
        log.warn("Deleting tag id={}", id);
        if (!tagRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        tagRepository.deleteById(id);
    }

    private String generateUniqueSlug(String base, Long currentId) {
        log.debug("Generating unique slug for base='{}', currentId={}", base, currentId);
        String candidate = base;
        int counter = 1;
        while (true) {
            String finalCandidate = candidate;
            boolean exists = tagRepository.findBySlug(finalCandidate)
                    .filter(t -> currentId == null || !t.getId().equals(currentId))
                    .isPresent();
            if (!exists) {
                log.debug("Using slug='{}' for tag (currentId={})", finalCandidate, currentId);
                return finalCandidate;
            }
            counter++;
            candidate = base + "-" + counter;
        }
    }
}
