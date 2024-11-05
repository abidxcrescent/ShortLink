package org.myshortlink;


import org.myshortlink.Service.ShortURLService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ShortURLServiceTest {

    private ShortURLService shortURLService;

    @BeforeEach
    public void setUp() {
        shortURLService = new ShortURLService(10); // Passing the concurrency limit
    }

    @Test
    public void testEncodeAndDecode() {
        // Given a long URL
        String longUrl = "https://example.com/library/react";

        // Encode the URL
        String shortUrl = shortURLService.encode(longUrl);

        String decodedUrl = shortURLService.decode(shortUrl);

        assertNotNull(shortUrl);
        assertEquals(longUrl, decodedUrl);
    }

    @Test
    public void testConcurrencyLimit() throws InterruptedException, ExecutionException {
        final String longUrl = "https://example.com";
        final int maxConcurrentRequests = 10;
        final int totalRequests = 11;  // One more than the concurrency limit

        ExecutorService executor = Executors.newFixedThreadPool(totalRequests);

        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < totalRequests; i++) {
            futures.add(executor.submit(() -> shortURLService.processRequest(longUrl, true,"127.0.0.1")));
        }


        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        for (Future<String> future : futures) {
            String response = future.get();

            if (response.contains("{\"error\": \"Too many concurrent requests\"}")) {
                failureCount.incrementAndGet();
            } else {
                successCount.incrementAndGet();
            }
        }

        executor.shutdown();

        assertEquals(maxConcurrentRequests, successCount.get()-1);
    }
}

