package com.px.jfmbackend.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.px.jfmbackend.entity.TagEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TagRepoTests {
  @Autowired private TagRepo tagRepo;

  private Long savedId;

  @BeforeEach
  public void setup() {
    TagEntity tag = new TagEntity();
    tag.setName("test1");
    savedId = tagRepo.save(tag).getId();
    assertNotNull(savedId);
  }

  @Test
  void testFindById_found() {
    Optional<TagEntity> found = tagRepo.findById(savedId);

    assertTrue(found.isPresent());
    assertEquals(savedId, found.get().getId());
    assertEquals("test1", found.get().getName());
  }

  @Test
  void testFindById_notFound() {
    Optional<TagEntity> found = tagRepo.findById(999999L);

    assertTrue(found.isEmpty());
  }

  @Test
  void testFindByName_found() {
    Optional<TagEntity> found = tagRepo.findByName("test1");

    assertTrue(found.isPresent());
    assertEquals("test1", found.get().getName());
    assertNotNull(found.get().getId());
  }

  @Test
  void testFindByName_notFound() {
    Optional<TagEntity> found = tagRepo.findByName("nope");

    assertTrue(found.isEmpty());
  }

  @Test
  void testUniqueNameConstraint_duplicateThrows() {
    // insert the same name, should violate unique constraint
    TagEntity dup = new TagEntity("test1");

    assertThrows(Exception.class, () -> tagRepo.saveAndFlush(dup));
  }

  @Test
  void testDeleteAllById_found() {
    // Make sure there exists such tag in db
    Optional<TagEntity> found = tagRepo.findById(savedId);
    assertTrue(found.isPresent());

    // Make a list of id to be deleted
    List<Long> ids = new ArrayList<>();
    ids.add(savedId);
    tagRepo.deleteAllById(ids);

    assertFalse(tagRepo.findById(savedId).isPresent());
  }

  @Test
  void testDeleteAllById_notFound() {
    Long idNotExist = 999999L;
    Optional<TagEntity> found = tagRepo.findById(idNotExist);
    assertFalse(found.isPresent());

    List<Long> ids = new ArrayList<>();
    ids.add(idNotExist);
    tagRepo.deleteAllById(ids);

    assertFalse(tagRepo.findById(idNotExist).isPresent());
  }
}
