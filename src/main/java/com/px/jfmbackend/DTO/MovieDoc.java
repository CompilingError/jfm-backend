package com.px.jfmbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movies")
public class MovieDoc {
    @Id
    private String id;
    private String name;
    private String path;

    private String description;
    private ArrayList<String> tags;

    private ArrayList<String> actors;

    // Recommendation System
    private int freshVal;
}