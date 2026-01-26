package com.px.jfmbackend.entity;

import com.px.jfmbackend.entity.baseEntity.BasePropertyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class TagEntity extends BasePropertyEntity {

  public TagEntity(String name) {
    super(name);
  }
}
