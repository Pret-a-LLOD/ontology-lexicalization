package de.citec.sc.generator.restservice;

import de.citec.generator.config.ConfigDownload;
import de.citec.generator.config.ConfigLemon;
import de.citec.generator.config.ConfigLex;
import de.citec.generator.config.Constants;
import de.citec.generator.results.ResultDownload;
import de.citec.generator.results.ResultJsonLD;
import de.citec.generator.results.ResultLex;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class SchemaController {
    
    
    @RequestMapping(path = "/download", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultDownload downloadData(@RequestBody ConfigDownload conf) {
        return new ResponseTransfer().downloadData(conf);
    }

    @RequestMapping(path = "/lexicalization", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultLex lexicalization(@RequestBody ConfigLex conf) {
        return new ResponseTransfer().lexicalization(conf);

    }

    @RequestMapping(path = "/createLemon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createLemon(@RequestBody ConfigLemon conf) {
        return new ResponseTransfer().createLemon(conf);
    }
    
    @RequestMapping(path = "/searchPattern", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String searchLemon(@RequestBody ConfigDownload conf) {
        return new ResponseTransfer().searchLemon(conf);
    }
  
}
