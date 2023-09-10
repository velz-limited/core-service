package com.velz.service.core.configuration.converters;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
@ConfigurationPropertiesBinding
public class RSAPrivateKeyConverter implements Converter<String, RSAPrivateKey> {

    @Override
    public RSAPrivateKey convert(String source) {
        if (isEmpty(source)) {
            return null;
        }

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(source.getBytes());
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }
}
