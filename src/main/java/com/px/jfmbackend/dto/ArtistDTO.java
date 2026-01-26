package com.px.jfmbackend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO implements Serializable {
  private Long id;
  private String name;

  @JsonCreator
  public ArtistDTO(String name) {
    this.name = name;
  }
}
