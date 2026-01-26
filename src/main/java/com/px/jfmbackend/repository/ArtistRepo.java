package com.px.jfmbackend.repository;

import com.px.jfmbackend.entity.ArtistEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepo extends JpaRepository<ArtistEntity, Long> {
  Optional<ArtistEntity> findByName(String name);

  boolean existsByName(String name);

  boolean existsById(Long id);
}
