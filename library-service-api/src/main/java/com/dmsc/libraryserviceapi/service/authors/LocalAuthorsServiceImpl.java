package com.dmsc.libraryserviceapi.service.authors;

import com.dmsc.libraryserviceapi.entity.AuthorEntity;
import com.dmsc.libraryserviceapi.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalAuthorsServiceImpl implements LocalAuthorsService {

    private final AuthorRepository authorRepository;

    public LocalAuthorsServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    @Override
    public AuthorEntity findOrCreateAuthorByName(String name) {
        return authorRepository.findByName(name)
            .orElseGet(() -> {
                AuthorEntity entity = new AuthorEntity();
                entity.setName(name);
                return authorRepository.save(entity);
            });
    }
}
