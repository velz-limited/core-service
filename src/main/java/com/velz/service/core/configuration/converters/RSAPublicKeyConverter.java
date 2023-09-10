package com.velz.service.core.configuration.converters;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
@ConfigurationPropertiesBinding
public class RSAPublicKeyConverter implements Converter<String, RSAPublicKey> {

    @Override
    public RSAPublicKey convert(String source) {
        if (isEmpty(source)) {
            return null;
        }

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(source.getBytes());
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }
}
