package com.px.jfmbackend.repository;

import com.px.jfmbackend.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepo extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByName(String name);
    Optional<TagEntity> findById(Long id);
}
