package com.px.jfmbackend.repository;

import com.px.jfmbackend.entity.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActorRepo extends JpaRepository<ActorEntity, Long> {
    Optional<ActorEntity> findByName(String name);
    Optional<ActorEntity> findById(Long id);
}
