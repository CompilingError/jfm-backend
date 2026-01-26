package com.px.jfmbackend.dto;

import java.util.List;

public record MovieUpdateDTO(
    String name,
    String path,
    String description,
    List<Long> tagIds,
    List<Long> artistIds,
    Integer freshVal,
    Boolean like) {}
