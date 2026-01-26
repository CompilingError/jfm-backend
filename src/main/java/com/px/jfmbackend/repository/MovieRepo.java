package com.px.jfmbackend.repository;

import com.px.jfmbackend.entity.MovieFileEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepo extends JpaRepository<MovieFileEntity, Long> {
  List<MovieFileEntity> findByNameIgnoreCase(String name);

  boolean existsByContentHash(String contentHash);

  @Query(
      """
      select distinct m from MovieFileEntity m
      join m.tags t
      where t.id in :tagIds
      """)
  Page<MovieFileEntity> findAllHavingAnyTag(@Param("tagIds") List<Long> tagIds, Pageable pageable);

  @Query(
      """
      select m from MovieFileEntity m
      join m.tags t
      where t.id in :tagIds
      group by m.id
      having count(distinct t.id) = :tagCount
      """)
  Page<MovieFileEntity> findAllHavingAllTags(
      @Param("tagIds") List<Long> tagIds, @Param("tagCount") long tagCount, Pageable pageable);

  @Query(
      """
      select distinct m from MovieFileEntity m
      join m.artists a
      where a.id in :artistIds
      """)
  Page<MovieFileEntity> findAllHavingAnyArtist(
      @Param("artistIds") List<Long> artistIds, Pageable pageable);

  @Query(
      """
      select m from MovieFileEntity m
      join m.artists a
      where a.id in :artistIds
      group by m.id
      having count(distinct a.id) = :artistCount
      """)
  Page<MovieFileEntity> findAllHavingAllArtists(
      @Param("artistIds") List<Long> artistIds,
      @Param("artistCount") long artistCount,
      Pageable pageable);
}
