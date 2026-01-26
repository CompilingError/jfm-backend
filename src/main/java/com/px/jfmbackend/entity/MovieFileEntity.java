package com.px.jfmbackend.entity;

import com.px.jfmbackend.entity.baseEntity.BaseFileEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
@EntityListeners(AuditingEntityListener.class)
public class MovieFileEntity extends BaseFileEntity {

  private boolean like;

  // Recommendation System
  private int freshVal;

  @ManyToMany
  @JoinTable(
      name = "movie_tags",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<TagEntity> tags = new HashSet<>();

  @ManyToMany
  @JoinTable(
      name = "movie_artists",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "artist_id"))
  private Set<ArtistEntity> artists = new HashSet<>();
}
