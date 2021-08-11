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
public class SchemaController implements Constants{
    
    
    @RequestMapping(path = ENDPOINT_DOWNLOAD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultDownload downloadData(@RequestBody ConfigDownload conf) {
        return new ResponseTransfer().downloadData(conf);
    }

    @RequestMapping(path =ENDPOINT_LEX, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultLex lexicalization(@RequestBody ConfigLex conf) {
        return new ResponseTransfer().lexicalization(conf);

    }

    @RequestMapping(path = ENDPOINT_CREATE_LEMON, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createLemon(@RequestBody ConfigLemon conf) {
        return new ResponseTransfer().createLemon(conf);
    }
    
    @RequestMapping(path = ENDPOINT_SEARCH_PATTERN, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String searchLemon(@RequestBody ConfigDownload conf) {
        return new ResponseTransfer().searchLemon(conf);
    }
    
    @RequestMapping(path = ENDPOINT_QUESTION_ANSWER_LEX_ENTRY, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultLex createLexicalEntry(@RequestBody ConfigLemon conf) {
        return new ResponseTransfer().createLexicalEntry(conf);
    }
    
    @RequestMapping(path = ENDPOINT_QUESTION_ANSWER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultLex createQuestionAnswer(@RequestBody ConfigLemon conf) {
        return new ResponseTransfer().createQuestionAnswer(conf);
    }
   
  
}
