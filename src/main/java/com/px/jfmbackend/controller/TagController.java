package com.px.jfmbackend.controller;

import com.px.jfmbackend.dto.CreateTagDTO;
import com.px.jfmbackend.dto.TagDTO;
import com.px.jfmbackend.dto.UpdateTagDTO;
import com.px.jfmbackend.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tags")
public class TagController {

  private final TagService tagService;

  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  // Get all tags
  @GetMapping
  public List<TagDTO> getTags() {
    return tagService.findAll();
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<TagDTO> getTagByName(@PathVariable String name) {
    return tagService
        .findByName(name)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<TagDTO> getTagById(@PathVariable long id) {
    return tagService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<TagDTO> createTag(@RequestBody CreateTagDTO createTagDTO) {
    TagDTO createdTag = tagService.create(createTagDTO);

    return ResponseEntity.ok().body(createdTag);
  }

  @PutMapping("/id/{id}")
  public ResponseEntity<TagDTO> updateTag(
      @PathVariable long id, @RequestBody UpdateTagDTO updateTagDTO) {
    return tagService
        .update(id, updateTagDTO)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
