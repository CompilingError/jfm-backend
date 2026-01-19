package com.px.jfmbackend.repository;

import com.px.jfmbackend.entity.MovieEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepo extends JpaRepository<MovieEntity, Long> {
  Optional<MovieEntity> findByName(String name);

  Optional<MovieEntity> findById(Long id);
}
