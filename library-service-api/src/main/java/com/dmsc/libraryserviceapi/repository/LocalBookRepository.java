package com.dmsc.libraryserviceapi.repository;

import com.dmsc.libraryserviceapi.entity.LocalBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalBookRepository extends JpaRepository<LocalBookEntity, Long> {
    List<LocalBookEntity> findAllByTitleEqualsIgnoreCase(String title);
}
