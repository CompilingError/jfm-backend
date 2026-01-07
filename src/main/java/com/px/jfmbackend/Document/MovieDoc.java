package com.px.jfmbackend.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "movies")
public class MovieDoc {
    @Id
    private String id;
    private String name;

    @Indexed(unique = true)
    private String path;

    private String description;
    private ArrayList<String> tagIds;
    private ArrayList<String> actorIds;

    @CreatedDate
    private Instant createdAt;

    // Recommendation System
    private int freshVal;
}