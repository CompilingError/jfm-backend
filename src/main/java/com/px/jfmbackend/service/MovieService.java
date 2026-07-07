package com.px.jfmbackend.service;

import com.px.jfmbackend.dto.ArtistDTO;
import com.px.jfmbackend.dto.MovieCreateDTO;
import com.px.jfmbackend.dto.MovieDTO;
import com.px.jfmbackend.dto.MovieUpdateDTO;
import com.px.jfmbackend.dto.TagDTO;
import com.px.jfmbackend.entity.MovieFileEntity;
import com.px.jfmbackend.exception.DuplicateFileImportException;
import com.px.jfmbackend.exception.IdNotFoundException;
import com.px.jfmbackend.repository.ArtistRepo;
import com.px.jfmbackend.repository.MovieRepo;
import com.px.jfmbackend.repository.TagRepo;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

  private final Logger logger = Logger.getLogger(MovieService.class.getName());

  private final MovieRepo movieRepo;
  private final TagRepo tagRepo;
  private final ArtistRepo artistRepo;

  private final FreshValService freshValService;

  @Autowired
  public MovieService(
      MovieRepo movieRepo,
      TagRepo tagRepo,
      ArtistRepo artistRepo,
      FreshValService freshValService) {
    this.movieRepo = movieRepo;
    this.tagRepo = tagRepo;
    this.artistRepo = artistRepo;
    this.freshValService = freshValService;
  }

  public Page<MovieDTO> findAll(Pageable pageable) {
    return movieRepo.findAll(pageable).map(this::toDto);
  }

  public Optional<MovieDTO> findById(Long id) {
    return movieRepo.findById(id).map(this::toDto);
  }

  public List<MovieDTO> findByName(String name) {
    return movieRepo.findByNameIgnoreCase(name).stream().map(this::toDto).toList();
  }

  public Page<MovieDTO> findByTags(List<Long> tagIds, String mode, Pageable pageable) {
    if (tagIds == null || tagIds.isEmpty()) {
      return movieRepo.findAll(pageable).map(this::toDto);
    }

    if ("ANY".equalsIgnoreCase(mode)) {
      return movieRepo.findAllHavingAnyTag(tagIds, pageable).map(this::toDto);
    }

    return movieRepo.findAllHavingAllTags(tagIds, tagIds.size(), pageable).map(this::toDto);
  }

  public Page<MovieDTO> findByArtists(List<Long> artistIds, String mode, Pageable pageable) {
    if (artistIds == null || artistIds.isEmpty()) {
      return movieRepo.findAll(pageable).map(this::toDto);
    }

    if ("ANY".equalsIgnoreCase(mode)) {
      return movieRepo.findAllHavingAnyArtist(artistIds, pageable).map(this::toDto);
    }

    return movieRepo
        .findAllHavingAllArtists(artistIds, artistIds.size(), pageable)
        .map(this::toDto);
  }

  public Page<MovieDTO> searchMovies(
      String name,
      List<Long> tagIds,
      String tagMode,
      List<Long> artistIds,
      String artistMode,
      Boolean liked,
      Integer minFreshVal,
      Integer maxFreshVal,
      Pageable pageable) {
    String normalizedName = normalizeSearchText(name);

    List<Long> candidateIds = buildCandidateMovieIds(tagIds, tagMode, artistIds, artistMode);

    Page<MovieFileEntity> moviePage;

    if (candidateIds != null) {
      if (candidateIds.isEmpty()) {
        return Page.empty(pageable);
      }

      moviePage =
          movieRepo.searchWithCandidateIds(
              candidateIds, normalizedName, liked, minFreshVal, maxFreshVal, pageable);
    } else {
      moviePage =
          movieRepo.searchWithoutCandidateIds(
              normalizedName, liked, minFreshVal, maxFreshVal, pageable);
    }

    return moviePage.map(this::toDto);
  }

  private List<Long> buildCandidateMovieIds(
      List<Long> tagIds, String tagMode, List<Long> artistIds, String artistMode) {
    Set<Long> candidateIds = null;

    if (hasIds(tagIds)) {
      List<Long> tagMovieIds = findMovieIdsByTags(tagIds, tagMode);
      candidateIds = new HashSet<>(tagMovieIds);
    }

    if (hasIds(artistIds)) {
      List<Long> artistMovieIds = findMovieIdsByArtists(artistIds, artistMode);

      if (candidateIds == null) {
        candidateIds = new HashSet<>(artistMovieIds);
      } else {
        candidateIds.retainAll(artistMovieIds);
      }
    }

    if (candidateIds == null) {
      return null;
    }

    return new ArrayList<>(candidateIds);
  }

  private List<Long> findMovieIdsByTags(List<Long> tagIds, String tagMode) {
    String normalizedMode = normalizeMode(tagMode, "ALL");

    if ("ANY".equals(normalizedMode)) {
      return movieRepo.findMovieIdsHavingAnyTag(tagIds);
    }

    return movieRepo.findMovieIdsHavingAllTags(tagIds, tagIds.size());
  }

  private List<Long> findMovieIdsByArtists(List<Long> artistIds, String artistMode) {
    String normalizedMode = normalizeMode(artistMode, "ANY");

    if ("ALL".equals(normalizedMode)) {
      return movieRepo.findMovieIdsHavingAllArtists(artistIds, artistIds.size());
    }

    return movieRepo.findMovieIdsHavingAnyArtist(artistIds);
  }

  private boolean hasIds(List<Long> ids) {
    return ids != null && !ids.isEmpty();
  }

  private String normalizeMode(String mode, String defaultMode) {
    if (mode == null || mode.isBlank()) {
      return defaultMode;
    }

    String upperMode = mode.trim().toUpperCase();

    if ("ALL".equals(upperMode) || "ANY".equals(upperMode)) {
      return upperMode;
    }

    return defaultMode;
  }

  private String normalizeSearchText(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }

    return value.trim();
  }

  @Transactional
  public MovieDTO create(MovieCreateDTO req)
      throws DuplicateFileImportException, IllegalArgumentException {
    // Name/Path non-null check
    String name = req.name() == null ? "" : req.name().trim();
    String path = req.path() == null ? "" : req.path().trim();
    if (name.isBlank() || path.isBlank()) {
      throw new IllegalArgumentException("name/path cannot be blank");
    }

    // Hash Check
    // @TODO Use real content has later when frontend is finished
    String contentHash = sha256HexOfString(path);

    if (movieRepo.existsByContentHash(contentHash)) {
      throw new DuplicateFileImportException("File already exists (duplicate contentHash).");
    }

    // Basic properties
    MovieFileEntity movie = new MovieFileEntity();
    movie.setName(name);
    movie.setPath(path);
    movie.setContentHash(contentHash);
    movie.setDescription(req.description());

    // default
    movie.setLike(false);
    movie.setFreshVal(0);
    movie.setFreshValUpdatedAt(Instant.now());

    // Link tags/artists
    if (req.tagIds() != null && !req.tagIds().isEmpty()) {
      movie.setTags(new HashSet<>(tagRepo.findAllById(req.tagIds())));
    }
    if (req.artistIds() != null && !req.artistIds().isEmpty()) {
      movie.setArtists(new HashSet<>(artistRepo.findAllById(req.artistIds())));
    }

    MovieFileEntity saved = movieRepo.save(movie);
    return toDto(saved);
  }

  @Transactional
  public MovieDTO update(Long id, MovieUpdateDTO req) throws IdNotFoundException {
    MovieFileEntity movie =
        movieRepo
            .findById(id)
            .orElseThrow(
                () -> new IdNotFoundException("The movie with id " + id + " was not found."));

    if (req.name() != null) movie.setName(req.name().trim());
    if (req.path() != null) movie.setPath(req.path().trim());
    if (req.description() != null) movie.setDescription(req.description());

    if (req.freshVal() != null) movie.setFreshVal(req.freshVal());
    if (req.like() != null) movie.setLike(req.like());

    if (req.tagIds() != null) {
      movie.setTags(new HashSet<>(tagRepo.findAllById(req.tagIds())));
    }
    if (req.artistIds() != null) {
      movie.setArtists(new HashSet<>(artistRepo.findAllById(req.artistIds())));
    }

    return toDto(movie);
  }

  @Transactional
  public void delete(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return;
    }

    List<MovieFileEntity> movies = movieRepo.findAllById(ids);

    for (MovieFileEntity movie : movies) {
      movie.getTags().clear();
      movie.getArtists().clear();
    }

    movieRepo.deleteAll(movies);
    movieRepo.flush();
  }

  @Transactional
  public void updateAllFreshVals() {
    List<MovieFileEntity> all = movieRepo.findAll();
    for (MovieFileEntity movieFileEntity : all) {
      freshValService.applyAgingIfNeeded(movieFileEntity);
    }
  }

  @Transactional
  public void updateFreshValWatched(Long id) throws IdNotFoundException {
    MovieFileEntity movie =
        movieRepo
            .findById(id)
            .orElseThrow(() -> new IdNotFoundException("Movie with id \"" + id + "\" not found"));

    movie.setFreshVal(Math.max(0, movie.getFreshVal() - 30));
    movie.setLastWatchedAt(Instant.now());
    movie.setFreshValUpdatedAt(Instant.now());
  }

  private MovieDTO toDto(MovieFileEntity m) {
    return new MovieDTO(
        m.getId(),
        m.getName(),
        m.getPath(),
        m.getDescription(),
        m.getTags().stream().map(t -> new TagDTO(t.getId(), t.getName())).toList(),
        m.getArtists().stream().map(a -> new ArtistDTO(a.getId(), a.getName())).toList(),
        m.isLike(),
        m.getFreshVal());
  }

  private static String sha256HexOfString(String input) {
    try {
      var digest = java.security.MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
      var sb = new StringBuilder();
      for (byte b : hash) sb.append(String.format("%02x", b));
      return sb.toString();
    } catch (Exception e) {
      throw new RuntimeException("Failed to hash", e);
    }
  }
}
