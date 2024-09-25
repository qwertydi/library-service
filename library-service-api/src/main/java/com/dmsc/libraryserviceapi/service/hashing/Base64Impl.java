package com.dmsc.libraryserviceapi.service.hashing;

import com.dmsc.libraryserviceapi.model.book.BookSystemEnum;
import com.dmsc.libraryserviceapi.util.IdGeneratorUtil;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class Base64Impl implements IdentifierHashService {

    @Override
    public String hash(BookSystemEnum bookSystemEnum, String id) {
        return Base64.getUrlEncoder()
            .encodeToString(IdGeneratorUtil.build(bookSystemEnum, id).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Optional<MultiValueMap<String, String>> getDetailsFromHash(String hashedString) {
        return IdGeneratorUtil.getDetailsFromBookId(
            new String(Base64.getDecoder().decode(hashedString), StandardCharsets.UTF_8)
        );
    }
}
