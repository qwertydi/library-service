package com.dmsc.libraryserviceapi.configuration;

import com.dmsc.libraryserviceapi.service.hashing.AESIdentifierHashImpl;
import com.dmsc.libraryserviceapi.service.hashing.Base64Impl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
public class HashingConfiguration {

    @Bean
    @ConfigurationProperties(prefix = HashingProperties.PREFIX)
    @Validated
    public HashingProperties hashingProperties() {
        return new HashingProperties();
    }

    @Bean
    @ConditionalOnProperty(name = "library.identifier.hashing.type", havingValue = "AES")
    public AESIdentifierHashImpl aesIdentifierHash(HashingProperties hashingProperties) {
        return new AESIdentifierHashImpl(hashingProperties.getKey());
    }

    @Bean
    @ConditionalOnProperty(name = "library.identifier.hashing.type", havingValue = "BASE64")
    public Base64Impl base64() {
        return new Base64Impl();
    }
}
