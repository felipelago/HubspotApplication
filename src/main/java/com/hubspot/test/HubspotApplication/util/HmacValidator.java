package com.hubspot.test.HubspotApplication.util;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class HmacValidator {

    public String calculateHmac(byte[] data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            // Converte o secret para bytes e usa o UTF-8 (certifique-se de que não haja espaços extras)
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hmacBytes = mac.doFinal(data);

            // Converter os bytes para uma string hexadecimal
            StringBuilder sb = new StringBuilder(hmacBytes.length * 2);
            for (byte b : hmacBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular HMAC", e);
        }
    }

    public boolean isValid(byte[] payloadBytes, String headerSignature, String secret) {
        String generated = calculateHmac(payloadBytes, secret);
        return generated.equals(headerSignature);
    }
}
