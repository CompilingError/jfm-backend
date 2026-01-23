package com.px.jfmbackend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO implements Serializable {
  private Long id;
  private String name;

  @JsonCreator
  public ArtistDTO(String name) {
    this.name=name;
  }
}
