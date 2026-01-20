package com.px.jfmbackend.repository;

import com.px.jfmbackend.entity.ActorEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepo extends JpaRepository<ActorEntity, Long> {
  Optional<ActorEntity> findByName(String name);

  Optional<ActorEntity> findById(Long id);
}
