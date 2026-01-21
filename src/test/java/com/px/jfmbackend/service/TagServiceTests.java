package com.px.jfmbackend.service;

import com.px.jfmbackend.dto.CreateTagDTO;
import com.px.jfmbackend.dto.TagDTO;
import com.px.jfmbackend.dto.UpdateTagDTO;
import com.px.jfmbackend.entity.TagEntity;
import com.px.jfmbackend.exception.IdNotFoundException;
import com.px.jfmbackend.exception.TagAlreadyExistException;
import com.px.jfmbackend.repository.TagRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTests {

    @Mock
    private TagRepo tagRepo;

    @InjectMocks
    private TagService tagService;

    @Test
    void create_success() {
        // arrange
        when(tagRepo.existsByName("test_tag_1")).thenReturn(false);

        TagEntity saved = new TagEntity();
        saved.setId(1L);
        saved.setName("test_tag_1");
        when(tagRepo.save(any(TagEntity.class))).thenReturn(saved);

        // act
        TagDTO result = tagService.create(new CreateTagDTO(" test_tag_1 ")); // test trim

        // assert
        assertEquals(1L, result.getId());
        assertEquals("test_tag_1", result.getName());

        // verify: save called with trimmed name
        ArgumentCaptor<TagEntity> captor = ArgumentCaptor.forClass(TagEntity.class);
        verify(tagRepo).save(captor.capture());
        assertEquals("test_tag_1", captor.getValue().getName());

        verify(tagRepo).existsByName("test_tag_1");
        verifyNoMoreInteractions(tagRepo);
    }

    @Test
    void create_duplicate_shouldThrow() {
        when(tagRepo.existsByName("test_tag_1")).thenReturn(true);

        assertThrows(
                TagAlreadyExistException.class,
                () -> tagService.create(new CreateTagDTO("test_tag_1"))
        );

        verify(tagRepo).existsByName("test_tag_1");
        verifyNoMoreInteractions(tagRepo);
    }

    @Test
    void findAll_mapsEntitiesToDTOs() {
        TagEntity t1 = new TagEntity();
        t1.setId(1L);
        t1.setName("test_tag_1");

        TagEntity t2 = new TagEntity();
        t2.setId(2L);
        t2.setName("test_tag_2");

        when(tagRepo.findAll()).thenReturn(List.of(t1, t2));

        List<TagDTO> result = tagService.findAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.getFirst().getId());
        assertEquals("test_tag_1", result.getFirst().getName());

        verify(tagRepo).findAll();
        verifyNoMoreInteractions(tagRepo);
    }

    @Test
    void update_success() {
        long id = 10L;

        // 新名字不冲突
        when(tagRepo.existsByName("thriller")).thenReturn(false);

        TagEntity existing = new TagEntity();
        existing.setId(id);
        existing.setName("test_tag_1");

        when(tagRepo.findById(id)).thenReturn(Optional.of(existing));

        TagEntity saved = new TagEntity();
        saved.setId(id);
        saved.setName("thriller");
        when(tagRepo.save(any(TagEntity.class))).thenReturn(saved);

        Optional<TagDTO> result = tagService.update(id, new UpdateTagDTO("thriller"));

        assertTrue(result.isPresent());
        assertEquals("thriller", result.get().getName());

        verify(tagRepo).existsByName("thriller");
        verify(tagRepo).findById(id);
        verify(tagRepo).save(existing); // same object, name 被 set 了
        verifyNoMoreInteractions(tagRepo);
    }

    @Test
    void update_duplicateName_shouldThrow() {
        when(tagRepo.existsByName("test_tag_1")).thenReturn(true);

        assertThrows(
                TagAlreadyExistException.class,
                () -> tagService.update(10L, new UpdateTagDTO("test_tag_1"))
        );

        verify(tagRepo).existsByName("test_tag_1");
        verifyNoMoreInteractions(tagRepo);
    }

    @Test
    void update_idNotFound_shouldReturnEmpty() {
        when(tagRepo.existsByName("thriller")).thenReturn(false);
        when(tagRepo.findById(10L)).thenReturn(Optional.empty());

        Optional<TagDTO> result = tagService.update(10L, new UpdateTagDTO("thriller"));

        assertTrue(result.isEmpty());

        verify(tagRepo).existsByName("thriller");
        verify(tagRepo).findById(10L);
        verifyNoMoreInteractions(tagRepo);
    }

    @Test
    void delete_success_callsDeleteAllByIdOnce() {
        List<Long> ids = List.of(1L, 2L, 3L);

        when(tagRepo.existsById(1L)).thenReturn(true);
        when(tagRepo.existsById(2L)).thenReturn(true);
        when(tagRepo.existsById(3L)).thenReturn(true);

        tagService.delete(ids);

        verify(tagRepo).existsById(1L);
        verify(tagRepo).existsById(2L);
        verify(tagRepo).existsById(3L);
        verify(tagRepo).deleteAllById(ids);
        verifyNoMoreInteractions(tagRepo);
    }

    @Test
    void delete_notFound_shouldThrow_andNotDeleteAnything() {
        List<Long> ids = List.of(1L, 999L);

        when(tagRepo.existsById(1L)).thenReturn(true);
        when(tagRepo.existsById(999L)).thenReturn(false);

        assertThrows(IdNotFoundException.class, () -> tagService.delete(ids));

        verify(tagRepo).existsById(1L);
        verify(tagRepo).existsById(999L);
        verify(tagRepo, never()).deleteAllById(any());
        verifyNoMoreInteractions(tagRepo);
    }
}
