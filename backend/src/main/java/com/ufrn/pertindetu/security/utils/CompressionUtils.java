package com.ufrn.pertindetu.security.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

/**
 * Utility class for handling Base64 decoding and data decompression.
 * <p>
 * Provides methods to decode Base64 strings, decompress byte arrays using
 * the Inflater algorithm, and a combined method to decode and decompress
 * a Base64-encoded compressed string.
 */
public final class CompressionUtils {

    private CompressionUtils() {
        throw new RuntimeException("Utility class");
    }

    /**
     * Decodes a Base64-encoded string into a byte array.
     *
     * @param base64 the Base64-encoded string
     * @return decoded byte array
     */
    public static byte[] decodeBase64(String base64) {
        return Base64.decodeBase64(base64);
    }

    /**
     * Decompresses a byte array using the Inflater algorithm.
     *
     * @param compressedData the compressed byte array
     * @return decompressed byte array
     * @throws IOException if decompression fails
     */
    public static byte[] decompress(byte[] compressedData) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
        InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = inflaterInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        inflaterInputStream.close();
        byteArrayOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Decodes a Base64 string and then decompresses it.
     *
     * @param base64 the Base64-encoded compressed string
     * @return the resulting decompressed string
     * @throws RuntimeException if decompression fails
     */
    public static String decodeAndDecompress(String base64) {
        byte[] decodedBytes = decodeBase64(base64);
        byte[] decompressedBytes;
        try {
            decompressedBytes = decompress(decodedBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error decompressing token ", e);
        }
        return new String(decompressedBytes);
    }
}

