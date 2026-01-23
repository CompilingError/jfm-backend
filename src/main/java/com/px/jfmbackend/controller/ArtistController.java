package com.px.jfmbackend.controller;

import com.px.jfmbackend.dto.ArtistDTO;
import com.px.jfmbackend.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) { this.artistService = artistService; }

    /**
     * Retrieves all artists.
     *
     * @return A list of ArtistDTO representing all artists.
     */
    @GetMapping
    public List<ArtistDTO> getTags() {
        return artistService.findAll();
    }

    /**
     * Retrieves a artist by its name.
     *
     * @param name The name of the artist to retrieve.
     * @return A ResponseEntity containing the ArtistDTO if found, or a 404 Not Found
     *         status if not found.
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ArtistDTO> getTagByName(@PathVariable String name) {
        return artistService
                .findByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a artist by its ID.
     *
     * @param id The ID of the artist to retrieve.
     * @return A ResponseEntity containing the ArtistDTO if found, or a 404 Not Found
     *         status if not found.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<ArtistDTO> getTagById(@PathVariable long id) {
        return artistService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new artist.
     *
     * @param createArtistDTO The DTO containing the details of the artist to be created.
     * @return A ResponseEntity containing the created ArtistDTO.
     */
    @PostMapping
    public ResponseEntity<ArtistDTO> createTag(@RequestBody ArtistDTO createArtistDTO) {
        ArtistDTO createdTag = artistService.create(createArtistDTO);

        return ResponseEntity.ok().body(createdTag);
    }

    /**
     * Updates an existing artist by its ID.
     *
     * @param id           The ID of the artist to be updated.
     * @param updateArtistDTO The DTO containing the updated details of the artist.
     * @return A ResponseEntity containing the updated ArtistDTO if found, or a 404 Not
     */
    @PutMapping("/id/{id}")
    public ResponseEntity<ArtistDTO> updateTag(
            @PathVariable long id, @RequestBody ArtistDTO updateArtistDTO) {
        return artistService
                .update(id, updateArtistDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTag(@RequestParam List<Long> ids) {
        artistService.delete(ids);
        return ResponseEntity.noContent().build();
    }


}


