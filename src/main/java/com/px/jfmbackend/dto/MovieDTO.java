package com.px.jfmbackend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
