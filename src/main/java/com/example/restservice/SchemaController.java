package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/", 
        consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE, 
        method = {RequestMethod.GET, RequestMethod.POST})
public class SchemaController {
    
    @RequestMapping(path = "/response", method = RequestMethod.POST)
    @ResponseBody
    public ResponseTransfer postResponseController(@RequestBody Configuration conf) {
        return new ResponseTransfer(conf);
     }
    
    /*@PostMapping("/response")
    @ResponseBody
    public ResponseTransfer postResponseController(@RequestBody LoginForm loginForm) {
        return new ResponseTransfer("Thanks For Posting!!!");
     }*/

  
    /*
    /*@RequestMapping(path = "/foo/{id}", produces = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.GET)
    public ThisIsAFooXML getAFooXml(@PathVariable String id) {
        ThisIsAFooXML foo = fooService.getAFoo(id);
        return foo;
    }
    
    @RequestMapping(path = "/foo/{id}", consumes = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.GET)
    public ThisIsAFooXML getAFooXml(@PathVariable String id) {
        ThisIsAFooXML foo = fooService.getAFoo(id);
        return foo;
    }*/
    
    /*
    @GetMapping("/schema/{type}")
    public Person schema_endpoint(
            @PathVariable(value = "type") String type
    ) {
        return new Person();
    }

    @GetMapping("/schema/arrayof/{n}/{type}")
    public Person[] arrayOfValues_endpoint(
            @PathVariable(value = "n") Integer n,
            @PathVariable(value = "type") String type
    ) {
        Person instances[] = new Person[n];

        for (int i = 0; i < n; i++) {
            instances[i] = new Person();
        }
        return instances;
    }*/
}
