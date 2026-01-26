package com.px.jfmbackend.dto;

import java.util.List;

public record MovieCreateDTO(
    String name, String path, String description, List<Long> tagIds, List<Long> artistIds) {}
