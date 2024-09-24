package com.dmsc.libraryserviceapi.model.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateBookRequest {
    @NotBlank
    private String title;
    @NotEmpty
    private List<String> authors;
    @NotNull
    private String publishYear;
    @NotEmpty
    private List<String> languages;
}
