package com.hr.controller;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> request) {

        String message = request.get("message").toLowerCase();
        String reply = "I am your HR assistant.";

        if (message.contains("leave")) {
            reply = "You can apply leave from Leave Management section.";
        } 
        else if (message.contains("salary")) {
            reply = "Salary details are available in payroll section.";
        }
        else if (message.contains("holiday")) {
            reply = "Holiday list is available in dashboard.";
        }
        else if (message.contains("hi") || message.contains("hello")) {
            reply = "Hello 😊 How can I assist you today?";
        }

        Map<String, String> response = new HashMap<>();
        response.put("reply", reply);

        return response;
    }
}