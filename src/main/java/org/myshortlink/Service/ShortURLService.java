package org.myshortlink.Service;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ShortURLService {

    private final int maxConcurrentRequests;
    private final Semaphore semaphore;
    private final Map<String, String> urlDatabase;
    private final String baseUrl = "http://short.est/";
    private final Random random;

    public ShortURLService(int maxConcurrentRequests) {
        this.maxConcurrentRequests = maxConcurrentRequests;
        this.semaphore = new Semaphore(maxConcurrentRequests);
        this.urlDatabase = new HashMap<>();
        this.random = new Random();
    }
    private boolean isRequestFromLocalhost(String remoteAddr) {
        return "127.0.0.1".equals(remoteAddr) || "::1".equals(remoteAddr);
    }
    public String encode(String longUrl) {
        String shortUrl = generateShortUrl();
        urlDatabase.put(shortUrl, longUrl);
        return shortUrl;
    }

    public String decode(String shortUrl) {
        return urlDatabase.get(shortUrl);
    }

    public String processRequest(String url, boolean isEncode, String remoteAddr) {

        if (!isRequestFromLocalhost(remoteAddr)) {
            return "{\"error\": \"Access restricted to localhost only\"}";
        }

        try {
            System.out.println("Attempting to acquire semaphore...");
            if (!semaphore.tryAcquire()) {
                System.out.println("Semaphore acquisition failed - too many concurrent requests.");
                return "{\"error\": \"Too many concurrent requests\"}";
            }

            try {
                System.out.println("Semaphore acquired.");
                if (isEncode) {
                    String shortUrl = encode(url);
                    return "{\"shortenedUrl\": \"" + baseUrl + shortUrl + "\"}";
                } else {
                    String originalUrl = decode(url);
                    if (originalUrl == null) {
                        return "{\"error\": \"Short URL not found\"}";
                    }
                    return "{\"originalUrl\": \"" + originalUrl + "\"}";
                }
            } finally {
                semaphore.release(); // Release the permit after processing the request
                System.out.println("Semaphore released.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder shortUrl = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            shortUrl.append(characters.charAt(index));
        }
        return shortUrl.toString();
    }
}
