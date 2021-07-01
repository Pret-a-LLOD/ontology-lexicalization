/**
 * ********************************************************************************
 * Copyright (c) 2016, Semantic Computing Group, Bielefeld University All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the Semantic Computing Group nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE MONNET PROJECT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ********************************************************************************
 */
package de.citec.sc.lemon.io;

import de.citec.sc.lemon.core.Language;

import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.lemon.utils.Templates;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


/**
 * 
 * @author swalter
 */
public class OLD_CSV_LexiconLoader {
    
        
        public  OLD_CSV_LexiconLoader()
        {		
	}
	
	public Lexicon loadFromFile(String file, Language language)
	{
            Lexicon lexicon = new Lexicon();
            
            try (Stream<String> stream = Files.lines(Paths.get(file))) {

                stream.forEach(line ->{
                    createEntry(line, lexicon, language);
                });

            } catch (IOException e) {
                    e.printStackTrace();
            }
            
            return lexicon;
        }

    private void createEntry(String line, Lexicon lexicon, Language language) {
        
        String[] tmp = line.split("\t");
        String label = tmp[0];
        String preposition = tmp[1];
        String pos = tmp[2];
        String frame = tmp[3];
        String uri = tmp[4];
        String arg_1 = tmp[5];
        String arg_2 = tmp[6];
        arg_1 = "http://lemon-model.net/lemon#subjOfProp";
        arg_2 = "http://lemon-model.net/lemon#objOfProp";
        //todo: check http://lemon-model.net/lemon#subjOfProp and obj mapping
        
        
        /*
        TODO: Dont call templates, but use the rich information from the export two create one "template" for a normal entry and one for the restriction class
        Also if URI is a URI from a Class, create different namespace/unique uri
        */

        int frequency = Integer.valueOf(tmp[7]);
        
        if(!pos.contains("http")) pos = "http://www.lexinfo.net/ontology/2.0/lexinfo#"+pos;
        if(!frame.contains("http")) frame = "http://www.lexinfo.net/ontology/2.0/lexinfo#"+frame;
        if(!arg_1.contains("http")) arg_1 = "http://www.lexinfo.net/ontology/2.0/lexinfo#"+arg_1;
        if(!arg_2.contains("http")) arg_2 = "http://www.lexinfo.net/ontology/2.0/lexinfo#"+arg_2;
        
        if(frame.contains("IntransitivePPFrame")){
            Templates.create_IntransitiveVerb_Entry(lexicon, label, arg_1,arg_2,preposition,uri,language, frequency);
        }
        else if(frame.contains("AdjectivePredicateFrame")){
            Templates.create_Adjective_Entry(lexicon, label, arg_1, arg_2, preposition, uri, language, frequency);
        }
        else if(frame.contains("TransitiveFrame")){
            //todo distinguishe between transitive verb and reflexive without frame
            Templates.create_TransitiveVerb_Entry(lexicon, label, arg_1, arg_2, uri, language, frequency);            
        }
        else if(frame.contains("NounPPFrame")){
            	Templates.create_NounWithPrep_Entry(lexicon, label, arg_1, arg_2, preposition, uri, language, frequency);
        }
        else if(frame.contains("NounPossessiveFrame")){
            Templates.create_NounPossessive_Entry(lexicon, label, arg_2, arg_1, uri, language, frequency);

        }
        else if(frame.contains("ReflexiveTransitivePPFrame")){
            Templates.create_ReflexiveTransitiveVerb_Entry( lexicon, label, arg_2, arg_1,  preposition,uri , language, frequency);

        }
        
        else{System.out.println(frame);}
    
    }
        
        
        
}

