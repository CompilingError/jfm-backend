package com.px.jfmbackend.entity;

import com.px.jfmbackend.entity.baseEntity.BasePropertyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "artists")
@EntityListeners(AuditingEntityListener.class)
public class ArtistEntity extends BasePropertyEntity {

  public ArtistEntity(String name) {
    super(name);
  }
}
