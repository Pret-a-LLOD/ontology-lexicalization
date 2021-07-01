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


import de.citec.sc.lemon.core.LexicalEntry;
import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.lemon.core.Provenance;
import de.citec.sc.lemon.core.Reference;
import de.citec.sc.lemon.core.Restriction;
import de.citec.sc.lemon.core.Sense;
import de.citec.sc.lemon.core.SenseArgument;
import de.citec.sc.lemon.core.Sentence;
import de.citec.sc.lemon.core.SimpleReference;
import de.citec.sc.lemon.core.SyntacticArgument;
import de.citec.sc.lemon.core.SyntacticBehaviour;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * 
 * @author swalter
 */
public class CSV_LexiconSerialization {
    
        public CSV_LexiconSerialization(){
           
        }
        
        
        /**
         * Serialization in the format:
         * Label
         * Preposition
         * POS
         * Frame
         * Uri
         * RestrictionClass
         * Subj
         * Obj
         * Arg_1
         * Arg_2
         * Frequency
         * Probability
         * SameAs (List of uris seperated by <p>)
         * Sentence (can be also a text, concatinating multiple sentences
         * 
         * @param lexicon
         * @param filename
         * @throws IOException 
         */
	public void serialize(Lexicon lexicon, String filename) throws IOException {
            
            List<String> lines = new ArrayList<>();
            
            for(LexicalEntry entry:lexicon.getEntries()){
                String label = "";
                String preposition = "";
                String pos = "";
                String frame = "";
                String uri = "";
                String restrictionClass = "";
                String sense1_argument = "";
                String sense1_value = "";
                String sense2_argument = "";
                String sense2_value = "";
                String syntactic1_argument = "";
                String syntactic1_value = "";
                String syntactic2_argument = "";
                String syntactic2_value = "";
                int frequency = 0;
                double confidence = 0.0;
                String sameAs = "";
                String sentence = "";
                
                
                label = entry.getCanonicalForm();
                try{
                    preposition = entry.getPreposition().getCanonicalForm();
                }
                catch (Exception e){preposition = "";} // if entry has no preposition
                pos = entry.getPOS();
                HashMap<Sense, HashSet<SyntacticBehaviour>> sense_behaviour = entry.getSenseBehaviours();
                for(Sense sense: sense_behaviour.keySet()){
                    HashSet<SyntacticBehaviour> behaviours = sense_behaviour.get(sense);
                    for(SyntacticBehaviour behaviour:behaviours){
                        frame = behaviour.getFrame();
                        Reference ref = sense.getReference();
                        if (ref instanceof de.citec.sc.lemon.core.Restriction){
                            Restriction reference = (Restriction) ref;
                            uri = reference.getProperty();
                            restrictionClass = reference.getValue();
                        }
                        else if(ref instanceof de.citec.sc.lemon.core.SimpleReference){
                            SimpleReference reference = (SimpleReference) ref;
                            uri = reference.getURI();
                            restrictionClass =  "";
                        
                            Set<SenseArgument> sense_arguments = sense.getSenseArgs();
                            //if we have a restriction class, we have two different behaviours, each with one argument and one frame
                            if(sense_arguments.size()>1){
                                Iterator iter = sense_arguments.iterator();
                                SenseArgument first = (SenseArgument) iter.next();
                                sense1_argument = first.getArgumenType();
                                sense1_value = first.getValue();
                                SenseArgument second = (SenseArgument) iter.next();
                                sense2_argument = second.getArgumenType();
                                sense2_value = second.getValue();

                                Set<SyntacticArgument> behaviour_arguments = behaviour.getSynArgs();
                                if(behaviour_arguments.size()>1){
                                    iter = behaviour_arguments.iterator();
                                    SyntacticArgument first_s = (SyntacticArgument) iter.next();
                                    syntactic1_argument = first_s.getArgumentType();
                                    syntactic1_value = first_s.getValue();
                                    SyntacticArgument second_s = (SyntacticArgument) iter.next();
                                    syntactic2_argument = second_s.getArgumentType();
                                    syntactic2_value = second_s.getValue();

                                    Provenance provenance = entry.getProvenance(sense);
                                    try{
                                        frequency = provenance.getFrequency();
                                    }
                                    catch(Exception e) {frequency = 1;} // if no frequency is given
                                    try{
                                        confidence = provenance.getConfidence();
                                    }
                                    catch(Exception e){confidence = 0.0;} // if no confidence is given
                                    for(Sentence s : provenance.getSentences()){
                                        sentence += " " + s.getSentence();
                                    }
                                    sentence = sentence.trim();

                                    for(String same_as:entry.getList_same_as()){
                                        sameAs += " " + same_as;
                                    }
                                    sameAs = sameAs.trim();


                                    lines.add(label+"\t"+preposition+"\t"+pos+"\t"+frame+"\t"+uri+"\t"+restrictionClass+"\t"+sense1_argument
                                            +"\t"+sense1_value+"\t"+sense2_argument+"\t"+sense2_value+"\t"+syntactic1_argument+"\t"+syntactic1_value
                                            +"\t"+syntactic2_argument+"\t"+syntactic2_value+"\t"+Integer.toString(frequency)+"\t"+Double.toString(confidence)
                                            +"\t"+sameAs+"\t"+sentence);
                                }

                            }
                        }
                        
                    }
                    
                }
                
            }
            
    
            
	
            Files.write(Paths.get(filename), lines, UTF_8, APPEND, CREATE);
		
	}

	

}
