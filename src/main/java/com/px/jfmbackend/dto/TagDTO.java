package com.px.jfmbackend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO implements Serializable {
  private long id;
  private String name;

  @JsonCreator
  public TagDTO(String name) {
    this.name = name;
  }
}
