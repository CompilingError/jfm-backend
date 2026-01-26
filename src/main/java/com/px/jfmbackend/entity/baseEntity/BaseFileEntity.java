package com.px.jfmbackend.entity.baseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(of = "contentHash", callSuper = false)
public abstract class BaseFileEntity extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String description;

  @Column(nullable = false, unique = true, length = 64)
  private String contentHash;

  @Column(nullable = false)
  private String path;
}
