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
        return BookResponse.builder()
            .authors(localBookEntity.getAuthors().stream().map(AuthorEntity::getName).toList())
            .languages(localBookEntity.getLanguages().stream().map(LanguagesEntity::getLanguage).toList())
            .publishYear(localBookEntity.getPublishYear())
            .id(IdGeneratorUtil.build(BookSystemEnum.LOCAL, String.valueOf(localBookEntity.getId())))
            .title(localBookEntity.getTitle())
            .build();
    }
}
