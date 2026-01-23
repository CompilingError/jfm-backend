package com.px.jfmbackend.controller;

import com.px.jfmbackend.dto.CreateTagDTO;
import com.px.jfmbackend.dto.TagDTO;
import com.px.jfmbackend.dto.UpdateTagDTO;
import com.px.jfmbackend.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagController {

  private final TagService tagService;

  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  /**
   * Retrieves all tags.
   * 
   * @return A list of TagDTO representing all tags.
   */
  @GetMapping
  public List<TagDTO> getTags() {
    return tagService.findAll();
  }

  /**
   * Retrieves a tag by its name.
   * 
   * @param name The name of the tag to retrieve.
   * @return A ResponseEntity containing the TagDTO if found, or a 404 Not Found
   *         status if not found.
   */
  @GetMapping("/name/{name}")
  public ResponseEntity<TagDTO> getTagByName(@PathVariable String name) {
    return tagService
        .findByName(name)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Retrieves a tag by its ID.
   * 
   * @param id The ID of the tag to retrieve.
   * @return A ResponseEntity containing the TagDTO if found, or a 404 Not Found
   *         status if not found.
   */
  @GetMapping("/id/{id}")
  public ResponseEntity<TagDTO> getTagById(@PathVariable long id) {
    return tagService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Creates a new tag.
   *
   * @param createTagDTO The DTO containing the details of the tag to be created.
   * @return A ResponseEntity containing the created TagDTO.
   */
  @PostMapping
  public ResponseEntity<TagDTO> createTag(@RequestBody CreateTagDTO createTagDTO) {
    TagDTO createdTag = tagService.create(createTagDTO);

    return ResponseEntity.ok().body(createdTag);
  }

  /**
   * Updates an existing tag by its ID.
   *
   * @param id           The ID of the tag to be updated.
   * @param updateTagDTO The DTO containing the updated details of the tag.
   * @return A ResponseEntity containing the updated TagDTO if found, or a 404 Not
   */
  @PutMapping("/id/{id}")
  public ResponseEntity<TagDTO> updateTag(
      @PathVariable long id, @RequestBody UpdateTagDTO updateTagDTO) {
    return tagService
        .update(id, updateTagDTO)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteTag(@RequestParam List<Long> ids) {
    tagService.delete(ids);
    return ResponseEntity.noContent().build();
  }
}
