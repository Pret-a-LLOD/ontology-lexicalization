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
    
    /*@RequestMapping(path = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String searchLemon(@RequestBody Configuration conf) {
        return new ResponseTransfer().searchLemon(conf);
    }*/
}
