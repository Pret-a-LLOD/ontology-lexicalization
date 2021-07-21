package de.citec.sc.generator.restservice;

import de.citec.generator.config.Configuration;
import de.citec.generator.config.Constants;
import de.citec.generator.results.LemonJsonLD;
import de.citec.generator.results.LexProcessResult;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        method = {RequestMethod.GET, RequestMethod.POST})
public class SchemaController {

    @RequestMapping(path = "/lexicalization", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public LexProcessResult lexicalization(@RequestBody Configuration conf) {
        return new ResponseTransfer().runLexicalization(conf);

    }
    
    @RequestMapping(path = "/createLemon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createLemon(@RequestBody Configuration conf) {
        return new ResponseTransfer().createLemon(conf);
    }

    /*@RequestMapping(path = "/response", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String processer(@RequestBody Configuration conf) {
        ResponseTransfer responseTransfer = new ResponseTransfer(conf);
        return responseTransfer.getJsonLDString();
    }*/

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
