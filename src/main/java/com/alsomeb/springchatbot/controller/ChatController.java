package com.alsomeb.springchatbot.controller;


import com.alsomeb.springchatbot.dto.ChatRequest;
import com.alsomeb.springchatbot.dto.ChatResponse;
import com.alsomeb.springchatbot.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Value("${openai.url}")
    private String url;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate;

    @Autowired
    public ChatController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //endpoint ../chat?prompt=Whats the temperature in Stockholm

    /*
        OpenAI requires this RequestBody:
        - Our ChatRequest Class Is built for this.
        {
            "model": "gpt-3.5-turbo",
            "messages": [{"role": "user", "content": "Say this is a test!"]
        }

        OpenAI part of response object our ChatResponse Class:
        "choices":[
                    {
                        "message":
                            {
                                "role":"assistant",
                                "content":"This is a test!"
                             }
                      ..
                  ]


     */
    @GetMapping
    public ResponseEntity<Message> promptOpenAI(@RequestParam String prompt) {
        final ChatRequest request = new ChatRequest(model, prompt);
        ChatResponse response = restTemplate.postForObject(url, request, ChatResponse.class);

        if(response != null) {
            return new ResponseEntity<>(
                    response.getChoices().get(0).getMessage(),
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
