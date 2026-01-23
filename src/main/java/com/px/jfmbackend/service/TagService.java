package com.px.jfmbackend.service;

import com.px.jfmbackend.dto.CreateTagDTO;
import com.px.jfmbackend.dto.TagDTO;
import com.px.jfmbackend.dto.UpdateTagDTO;
import com.px.jfmbackend.entity.TagEntity;
import com.px.jfmbackend.exception.IdNotFoundException;
import com.px.jfmbackend.exception.TagAlreadyExistException;
import com.px.jfmbackend.repository.TagRepo;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {

  private final TagRepo tagRepo;
  private final Logger logger = Logger.getLogger(TagService.class.getName());

  @Autowired
  public TagService(TagRepo tagRepo) {
    this.tagRepo = tagRepo;
  }

  public List<TagDTO> findAll() {
    return tagRepo.findAll().stream().map(tag -> new TagDTO(tag.getId(), tag.getName())).toList();
  }

  public Optional<TagDTO> findByName(String name) {
    return tagRepo.findByName(name).map(tag -> new TagDTO(tag.getId(), tag.getName()));
  }

  public Optional<TagDTO> findById(Long id) {
    return tagRepo.findById(id).map(tag -> new TagDTO(tag.getId(), tag.getName()));
  }

  /**
   * Creates a new tag.
   *
   * @param createTagDTO The DTO containing the name of the tag to be created.
   * @return The created TagDTO.
   * @throws IllegalArgumentException if a tag with the same name already exists.
   */
  public TagDTO create(CreateTagDTO createTagDTO) {
    String name = createTagDTO.name().trim();

    if (tagRepo.existsByName(name)) {
      throw new TagAlreadyExistException("Tag already exists with name: \"" + name + "\"");
    }

    TagEntity tagEntity = tagRepo.save(new TagEntity(name));
    return new TagDTO(tagEntity.getId(), tagEntity.getName());
  }

  // It is designed to update tags with existence id, so no id check
  /**
   * Updates the name of an existing tag identified by its ID.
   *
   * @param id           The ID of the tag to be updated.
   * @param updateTagDTO The DTO containing the new name for the tag.
   * @return An Optional containing the updated TagDTO if the tag exists, or empty
   *         if not.
   * @throws IdNotFoundException      if the tag with the specified ID does not
   *                                  exist.
   * @throws TagAlreadyExistException if a tag with the new name already exists.
   */
  public Optional<TagDTO> update(long id, UpdateTagDTO updateTagDTO)
      throws IdNotFoundException, TagAlreadyExistException {
    String newName = updateTagDTO.name();

    if (!tagRepo.existsById(id)) {
      throw new IdNotFoundException("Tag with id: " + id + " does not exist.");
    }

    if (tagRepo.existsByName(newName)) {
      throw new TagAlreadyExistException("Tag already exists with name: \"" + newName + "\"");
    }

    return tagRepo
        .findById(id)
        .map(
            tagToUpdate -> {
              tagToUpdate.setName(newName);
              TagEntity updatedTag = tagRepo.save(tagToUpdate);
              return new TagDTO(updatedTag.getId(), updatedTag.getName());
            });
  }

  @Transactional
  public void delete(List<Long> ids) throws IdNotFoundException {
    for (Long id : ids) {
      if (!tagRepo.existsById(id)) {
        throw new IdNotFoundException("Tag with id: " + id + " does not exist.");
      }
    }
    tagRepo.deleteAllById(ids);
  }
}
