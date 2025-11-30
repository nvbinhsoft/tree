package com.example.blog.post;

import com.example.blog.dto.TagRequest;
import com.example.blog.dto.TagResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/tags")
@Tag(name = "Admin Tags")
@SecurityRequirement(name = "bearerAuth")
public class TagAdminController {
    private static final Logger log = LoggerFactory.getLogger(TagAdminController.class);
    private final TagService tagService;

    public TagAdminController(TagService tagService) {
        this.tagService = tagService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "List all tags (admin)")
    public List<TagResponse> list() {
        log.info("GET /api/admin/tags");
        return tagService.listAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a tag")
    public ResponseEntity<TagResponse> create(@Valid @RequestBody TagRequest request) {
        log.info("POST /api/admin/tags name='{}'", request.getName());
        return ResponseEntity.ok(tagService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a tag")
    public TagResponse update(@PathVariable Long id, @Valid @RequestBody TagRequest request) {
        log.info("PUT /api/admin/tags/{} name='{}'", id, request.getName());
        return tagService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tag")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/admin/tags/{}", id);
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
