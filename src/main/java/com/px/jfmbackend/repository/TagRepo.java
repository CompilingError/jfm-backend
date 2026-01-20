package com.px.jfmbackend.repository;

import com.px.jfmbackend.entity.TagEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<TagEntity, Long> {
  Optional<TagEntity> findByName(String name);

  boolean existsByName(String name);

  boolean existsById(Long id);
}
