package com.dmsc.libraryserviceapi.repository;

import com.dmsc.libraryserviceapi.entity.LanguagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<LanguagesEntity, Long> {
    Optional<LanguagesEntity> findByLanguage(String language);
}
