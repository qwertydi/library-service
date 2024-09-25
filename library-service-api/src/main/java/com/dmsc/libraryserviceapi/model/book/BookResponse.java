package com.dmsc.libraryserviceapi.model.book;

import com.dmsc.libraryserviceapi.entity.AuthorEntity;
import com.dmsc.libraryserviceapi.entity.LanguagesEntity;
import com.dmsc.libraryserviceapi.entity.LocalBookEntity;
import com.dmsc.libraryserviceapi.util.IdGeneratorUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResponse {
    private String id;
    private String title;
    private List<String> authors;
    private Integer publishYear;
    private List<String> languages;

    public static BookResponse fromLocalBook(LocalBookEntity localBookEntity) {
        if (Objects.isNull(localBookEntity)) {
            throw new IllegalStateException("Illegal Entity");
        }

        List<String> listAuthors = localBookEntity.getAuthors()
            .stream()
            .map(AuthorEntity::getName)
            .toList();

        List<String> listLanguages = localBookEntity.getLanguages()
            .stream()
            .map(LanguagesEntity::getLanguage)
            .toList();

        return BookResponse.builder()
            .authors(listAuthors)
            .languages(listLanguages)
            .publishYear(localBookEntity.getPublishYear())
            .id(String.valueOf(localBookEntity.getId()))
            .title(localBookEntity.getTitle())
            .build();
    }
}
