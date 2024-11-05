package org.example.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.Service.ShortURLService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ShortURLController {
     private final ShortURLService shortURLService;

    public ShortURLController(ShortURLService shortURLService) {
        this.shortURLService = shortURLService;
    }


        @PostMapping("/encode")
        public String encode(@RequestBody String longUrl, HttpServletRequest request) {
            return shortURLService.processRequest(longUrl, true, request.getRemoteAddr());
        }

        @GetMapping("/decode/{shortUrl}")
        public String decode(@PathVariable String shortUrl, HttpServletRequest request) {
            return shortURLService.processRequest(shortUrl, false, request.getRemoteAddr());
        }
}
