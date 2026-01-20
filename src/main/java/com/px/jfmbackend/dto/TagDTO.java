package com.px.jfmbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagDTO {
  private long id;
  private String name;

  public TagDTO(String name) {
    this.name = name;
  }
}
