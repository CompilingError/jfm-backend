package com.px.jfmbackend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO extends AuditedDTO {
  private Long id;
  private String name;
  private String path;
  private String description;
  private List<TagDTO> tags;
  private List<ArtistDTO> artists;
  private boolean like;
  private int freshVal;
}
