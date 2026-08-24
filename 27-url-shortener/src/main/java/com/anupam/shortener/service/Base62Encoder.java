package com.anupam.shortener.service;

import org.springframework.stereotype.Component;

/**
 * Utility component that encodes and decodes numeric IDs using Base62 encoding.
 * <p>
 * Base62 uses the character set [0-9A-Za-z], producing compact, URL-safe strings.
 * This is used to generate short codes from auto-incremented database IDs.
 * </p>
 *
 * @author Anupam
 */
@Component
public class Base62Encoder {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();

    /**
     * Encodes a positive long value into a Base62 string.
     * <p>
     * Example: 12345 encodes to "3d7".
     * </p>
     *
     * @param value the positive long value to encode
     * @return the Base62-encoded string representation
     */
    public String encode(long value) {
        if (value == 0) {
            return String.valueOf(ALPHABET.charAt(0));
        }

        // Build the Base62 string by repeatedly taking modulus and dividing
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(ALPHABET.charAt((int) (value % BASE)));
            value /= BASE;
        }
        // Reverse because digits are extracted in least-significant-first order
        return sb.reverse().toString();
    }

    /**
     * Decodes a Base62 string back to its numeric long value.
     *
     * @param encoded the Base62-encoded string
     * @return the decoded long value
     */
    public long decode(String encoded) {
        long result = 0;
        for (char c : encoded.toCharArray()) {
            result = result * BASE + ALPHABET.indexOf(c);
        }
        return result;
    }
}
