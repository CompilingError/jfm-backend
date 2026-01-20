package com.px.jfmbackend.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record CreateTagDTO(@NotBlank String name) implements Serializable {}
;
