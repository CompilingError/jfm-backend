package com.px.jfmbackend.controller;

import com.px.jfmbackend.dto.MovieCreateDTO;
import com.px.jfmbackend.dto.MovieDTO;
import com.px.jfmbackend.dto.MovieUpdateDTO;
import com.px.jfmbackend.service.MovieService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {
  private final MovieService movieService;

  @Autowired
  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping
  public Page<MovieDTO> list(Pageable pageable) {
    return movieService.findAll(pageable);
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<MovieDTO> getById(@PathVariable Long id) {
    return movieService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Same name allowed
  @GetMapping("/name/{name}")
  public List<MovieDTO> getByName(@PathVariable String name) {
    return movieService.findByName(name);
  }

  // mode = ALL/ANY
  @GetMapping("/by-tags")
  public Page<MovieDTO> byTags(
      @RequestParam List<Long> tagIds,
      @RequestParam(defaultValue = "ALL") String mode,
      Pageable pageable) {
    return movieService.findByTags(tagIds, mode, pageable);
  }

  // mode = ALL/ANY
  @GetMapping("/by-artists")
  public Page<MovieDTO> byArtists(
      @RequestParam List<Long> artistIds,
      @RequestParam(defaultValue = "ANY") String mode,
      Pageable pageable) {
    return movieService.findByArtists(artistIds, mode, pageable);
  }

  @GetMapping("/search")
  public Page<MovieDTO> search(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) List<Long> tagIds,
      @RequestParam(defaultValue = "ALL") String tagMode,
      @RequestParam(required = false) List<Long> artistIds,
      @RequestParam(defaultValue = "ANY") String artistMode,
      @RequestParam(required = false, name = "like") Boolean liked,
      @RequestParam(required = false) Integer minFreshVal,
      @RequestParam(required = false) Integer maxFreshVal,
      Pageable pageable) {
    return movieService.searchMovies(
        name, tagIds, tagMode, artistIds, artistMode, liked, minFreshVal, maxFreshVal, pageable);
  }

  @PostMapping
  public ResponseEntity<MovieDTO> create(@RequestBody MovieCreateDTO req) {
    return ResponseEntity.ok().body(movieService.create(req));
  }

  @PutMapping("/id/{id}")
  public ResponseEntity<MovieDTO> update(@PathVariable Long id, @RequestBody MovieUpdateDTO req) {
    return ResponseEntity.ok().body(movieService.update(id, req));
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestParam List<Long> ids) {
    movieService.delete(ids);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/update-freshval")
  public ResponseEntity<Void> updateFreshVal() {
    movieService.updateAllFreshVals();
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/watched/{id}")
  public ResponseEntity<Void> watchedFreshVal(@PathVariable Long id) {
    movieService.updateFreshValWatched(id);
    return ResponseEntity.noContent().build();
  }
}
