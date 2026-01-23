package com.px.jfmbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private String id;
    private String name;
    private String path;
    private String description;
    private List<TagDTO> tags;
    private int freshVal;
}
