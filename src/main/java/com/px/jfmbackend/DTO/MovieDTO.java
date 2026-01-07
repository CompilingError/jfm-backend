package com.px.jfmbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MovieDTO {
    private String id;
    private String name;
    private String path;
    private String description;
    private List<TagDTO> tags;
    private List<ActorDTO> actors;
    private int freshVal;
}
