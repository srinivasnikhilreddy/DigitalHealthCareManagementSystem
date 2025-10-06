
package com.iss.controllers;

import com.iss.models.GroqChat;
import com.iss.services.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class GroqController
{
    @Autowired
    private GroqService groqService;

    @PostMapping("/request")
    public ResponseEntity<String> chat(@RequestBody GroqChat request)
    {
        return ResponseEntity.ok(groqService.getResponse(request.getMessage()));
    }
}
