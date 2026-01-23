package com.px.jfmbackend.service;

import com.px.jfmbackend.dto.ArtistDTO;
import com.px.jfmbackend.entity.ArtistEntity;
import com.px.jfmbackend.exception.IdNotFoundException;
import com.px.jfmbackend.exception.TagAlreadyExistException;
import com.px.jfmbackend.repository.ArtistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ArtistService {

    private final ArtistRepo artistRepo;
    private final Logger logger = Logger.getLogger(ArtistService.class.getName());

    @Autowired
    public ArtistService(ArtistRepo artistRepo) {
        this.artistRepo = artistRepo;
    }

    public List<ArtistDTO> findAll() {
        return artistRepo.findAll().stream().map(artist -> new ArtistDTO(artist.getId(), artist.getName())).toList();
    }

    public Optional<ArtistDTO> findByName(String name) {
        return artistRepo.findByName(name).map(artist -> new ArtistDTO(artist.getId(), artist.getName()));
    }

    public Optional<ArtistDTO> findById(Long id) {
        return artistRepo.findById(id).map(artist -> new ArtistDTO(artist.getId(), artist.getName()));
    }

    /**
     * Creates a new artist.
     *
     * @param createTagDTO The DTO containing the name of the artist to be created.
     * @return The created ArtistDTO.
     * @throws IllegalArgumentException if an artist with the same name already exists.
     */
    public ArtistDTO create(ArtistDTO createTagDTO) {
        String name = createTagDTO.getName().trim();

        if (artistRepo.existsByName(name)) {
            throw new TagAlreadyExistException("Tag already exists with name: \"" + name + "\"");
        }

        ArtistEntity tagEntity = artistRepo.save(new ArtistEntity(name));
        return new ArtistDTO(tagEntity.getId(), tagEntity.getName());
    }

    /**
     * Updates the name of an existing artist identified by its ID.
     *
     * @param id           The ID of the artist to be updated.
     * @param updateTagDTO The DTO containing the new name for the artist.
     * @return An Optional containing the updated ArtistDTO if the artist exists, or empty
     *         if not.
     * @throws IdNotFoundException      if the artist with the specified ID does not
     *                                  exist.
     * @throws TagAlreadyExistException if a artist with the new name already exists.
     */
    public Optional<ArtistDTO> update(long id, ArtistDTO updateTagDTO)
            throws IdNotFoundException, TagAlreadyExistException {
        String newName = updateTagDTO.getName();

        if (!artistRepo.existsById(id)) {
            throw new IdNotFoundException("Tag with id: " + id + " does not exist.");
        }

        if (artistRepo.existsByName(newName)) {
            throw new TagAlreadyExistException("Tag already exists with name: \"" + newName + "\"");
        }

        return artistRepo
                .findById(id)
                .map(
                        tagToUpdate -> {
                            tagToUpdate.setName(newName);
                            ArtistEntity updatedTag = artistRepo.save(tagToUpdate);
                            return new ArtistDTO(updatedTag.getId(), updatedTag.getName());
                        });
    }

    @Transactional
    public void delete(List<Long> ids) throws IdNotFoundException {
        for (Long id : ids) {
            if (!artistRepo.existsById(id)) {
                throw new IdNotFoundException("Tag with id: " + id + " does not exist.");
            }
        }
        artistRepo.deleteAllById(ids);
    }
}
