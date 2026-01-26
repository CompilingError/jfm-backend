package com.px.jfmbackend.controller;

import com.px.jfmbackend.dto.MovieCreateDTO;
import com.px.jfmbackend.dto.MovieDTO;
import com.px.jfmbackend.dto.MovieUpdateDTO;
import com.px.jfmbackend.service.MovieService;
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

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) { this.movieService = movieService; }

    // 1) 全部 + 排序/分页
    @GetMapping
    public Page<MovieDTO> list(Pageable pageable) {
        return movieService.findAll(pageable);
    }

    // 2) 按 id
    @GetMapping("/id/{id}")
    public ResponseEntity<MovieDTO> getById(@PathVariable Long id) {
        return movieService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3) 按 name（允许同名 -> List）
    @GetMapping("/name/{name}")
    public List<MovieDTO> getByName(@PathVariable String name) {
        return movieService.findByName(name);
    }

    // 4) 按 tagIds 过滤（ALL/ANY 可用 mode 参数）
    @GetMapping("/by-tags")
    public Page<MovieDTO> byTags(
            @RequestParam List<Long> tagIds,
            @RequestParam(defaultValue = "ALL") String mode,
            Pageable pageable) {
        return movieService.findByTags(tagIds, mode, pageable);
    }

    // 5) 按 artistIds 过滤（ALL/ANY 可用 mode 参数）
    @GetMapping("/by-artists")
    public Page<MovieDTO> byArtists(
            @RequestParam List<Long> artistIds,
            @RequestParam(defaultValue = "ANY") String mode,
            Pageable pageable) {
        return movieService.findByArtists(artistIds, mode, pageable);
    }

    // 6) 创建
    @PostMapping
    public ResponseEntity<MovieDTO> create(@RequestBody MovieCreateDTO req) {
        return ResponseEntity.ok().body(movieService.create(req));
    }

    // 7) 更新
    @PutMapping("/id/{id}")
    public ResponseEntity<MovieDTO> update(@PathVariable Long id, @RequestBody MovieUpdateDTO req) {
        return movieService
                .update(id, req)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 8) 删除
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam List<Long> ids) {
        movieService.delete(ids);
        return ResponseEntity.noContent().build();
    }
}

