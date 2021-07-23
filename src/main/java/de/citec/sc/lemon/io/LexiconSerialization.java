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

import java.text.SimpleDateFormat;

import org.apache.jena.vocabulary.RDF;
import org.apache.jena.rdf.model.Model;

import de.citec.sc.lemon.core.LexicalEntry;
import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.lemon.core.Preposition;
import de.citec.sc.lemon.core.Provenance;
import de.citec.sc.lemon.core.Reference;
import de.citec.sc.lemon.core.Restriction;
import de.citec.sc.lemon.core.Sense;
import de.citec.sc.lemon.core.SenseArgument;
import de.citec.sc.lemon.core.Sentence;
import de.citec.sc.lemon.core.SimpleReference;
import de.citec.sc.lemon.core.SyntacticArgument;
import de.citec.sc.lemon.core.SyntacticBehaviour;
import de.citec.sc.lemon.vocabularies.DBLEXIPEDIA;
import de.citec.sc.lemon.vocabularies.LEMON;
import de.citec.sc.lemon.vocabularies.LEXINFO;
import de.citec.sc.lemon.vocabularies.OWL;
import de.citec.sc.lemon.vocabularies.PROVO;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author swalter
 */
public class LexiconSerialization {
     private String baseURI = null;
    
        public LexiconSerialization(){
           
        }
        

	public void serialize(Lexicon lexicon, Model model) {
            
                this.baseURI = lexicon.getBaseURI();
                
                for(Preposition prep : lexicon.getPrepositions()){
                    model.add(model.createResource(baseURI), LEMON.entry, model.createResource(this.baseURI+"preposition_"+prep.getCanonicalForm()));
                    model.add(model.createResource(baseURI+"preposition_"+prep.getCanonicalForm()), LEMON.language,model.createLiteral(prep.getLanguage().toString().toLowerCase()));
                    model.add(model.createResource(baseURI+"preposition_"+prep.getCanonicalForm()), LEXINFO.partOfSpeech,model.createResource("http://www.lexinfo.net/ontology/2.0/lexinfo#preposition"));
                    model.add(model.createResource(baseURI+"preposition_"+prep.getCanonicalForm()), LEMON.canonicalForm, model.createResource(this.baseURI+"preposition_"+prep.getCanonicalForm()+"#CanonicalForm"));
                    //model.add(model.createResource(this.baseURI+"preposition_"+prep.getCanonicalForm()+"#CanonicalForm"), LEMON.writtenRep, model.createLiteral(prep.getCanonicalForm()+"@"+prep.getLanguage().toString().toLowerCase()));
                    model.add(model.createResource(baseURI+"preposition_"+prep.getCanonicalForm()+"#CanonicalForm"), LEMON.writtenRep, model.createLiteral(prep.getCanonicalForm()+"@"+prep.getLanguage().toString().toLowerCase()));
                    
                }
		
		for (LexicalEntry entry: lexicon.getEntries())
		{
                    if(!entry.getURI().contains(" ") && !entry.getURI().contains("[")&& !entry.getURI().contains("\"")){
                        boolean add_entry = true;
                        if(entry.getPreposition()!=null){
                            /*
                            igrnore entries with wired prepositions, such as %
                            */
                            if(!StringUtils.isAlpha(entry.getPreposition().getCanonicalForm())) add_entry = false;
                        }
                        if(add_entry){
                            serialize(entry,model,baseURI);
                            model.add(model.createResource(baseURI), LEMON.entry, model.createResource(entry.getURI()));
                            
                            	
                        }
                        
                    }
			
		}
		
		model.add(model.createResource(baseURI), RDF.type, LEMON.Lexicon);	
		
	}

	private void serialize(LexicalEntry entry, Model model, String baseURI) {
            
                int numberReturnedSentences = 5;
                int sentence_counter = 0;
                Set<String> syn_signatures = new HashSet<>();
                		
		model.add(model.createResource(entry.getURI()),RDF.type,LEMON.LexicalEntry);
                
                if(entry.getPreposition()!=null){
                    model.add(model.createResource(entry.getURI()), LEMON.marker, model.createResource(baseURI+"preposition_"+entry.getPreposition().getCanonicalForm()));
                    model.add(model.createResource(entry.getURI()), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(entry.getCanonicalForm()+" ("+entry.getPreposition().getCanonicalForm()+")"));
                }
                else{
                    if(entry.getURI().contains("_as_PossessiveNoun")){
                        model.add(model.createResource(entry.getURI()), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(entry.getCanonicalForm()+" (poss.)"));

                    }
                    else 
                        model.add(model.createResource(entry.getURI()), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(entry.getCanonicalForm()));
                }
		
                model.add(model.createResource(entry.getURI()), LEMON.language, model.createLiteral(entry.getLanguage().toString().toLowerCase()));

		model.add(model.createResource(entry.getURI()), LEMON.canonicalForm, model.createResource(entry.getURI()+"#CanonicalForm"));
		model.add(model.createResource(entry.getURI()+"#CanonicalForm"), LEMON.writtenRep, model.createLiteral(entry.getCanonicalForm()+"@"+entry.getLanguage().toString().toLowerCase()));
                if(!entry.getAlternativeForms().isEmpty()){
                    for(String alternativeForm : entry.getAlternativeForms()){
                        model.add(model.createResource(entry.getURI()+"#CanonicalForm"), LEMON.otherForm, model.createLiteral(alternativeForm+"@"+entry.getLanguage().toString().toLowerCase()));
                    }
                }

                for(String same_as_uri:entry.getList_same_as())  model.add(model.createResource(entry.getURI()), OWL.sameAs, model.createResource(same_as_uri));
                
                
		if (entry.getReferences().size()>0)
		{
			int ref_counter = 0;
                        for(Sense sense:entry.getSenseBehaviours().keySet()){
                            
                            Reference ref = sense.getReference();
                            ref_counter+=1;
                            //System.out.println("Sense"+Integer.toString(ref_counter)+":"+sense.toString());
                            model.add(model.createResource(entry.getURI()), LEMON.sense, model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)));
                            //recently deleted for deleting provinces
                            //Provenance provenance = entry.getProvenance(sense);
                            //recently change
                            //model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)), PROVO.generatedBy, model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)));
                            
                            SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ");	
                            //recently changed for deleting activity and province
                            //model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), RDF.type, PROVO.Activity);
                            
                            //recently deleted for deleting provinces
                            /*if (provenance.getStartedAtTime() != null) model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.startedAtTime, model.createLiteral(df.format(provenance.getStartedAtTime())));
                            if (provenance.getEndedAtTime() != null) model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.endedatTime, model.createLiteral(df.format(provenance.getEndedAtTime())));
                            if (provenance.getConfidence() != null) model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.confidence, model.createTypedLiteral(provenance.getConfidence()));
                            if (provenance.getAgent() != null) model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.associatedWith, model.createLiteral(provenance.getAgent()));
                            if (provenance.getFrequency() != null) model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.frequency, model.createTypedLiteral(provenance.getFrequency()));
                            if(provenance.getSentences()!=null){
                                
                               for(Sentence sentence : provenance.getShortestSentences(numberReturnedSentences)){
                                   sentence_counter+=1;
                                   model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.sentence, model.createResource(entry.getURI()+"#Sentence"+Integer.toString(sentence_counter)));
                                   model.add(model.createResource(entry.getURI()+"#Sentence"+Integer.toString(sentence_counter)), DBLEXIPEDIA.subjOfProp, model.createLiteral(sentence.getSubjOfProp()));
                                   model.add(model.createResource(entry.getURI()+"#Sentence"+Integer.toString(sentence_counter)), DBLEXIPEDIA.objOfProp, model.createLiteral(sentence.getObjOfProp()));
                                   model.add(model.createResource(entry.getURI()+"#Sentence"+Integer.toString(sentence_counter)), DBLEXIPEDIA.sentence, model.createLiteral(sentence.getSentence()));
                               }
                            }
                            
                            for(String pattern : provenance.getPatternset()) {
                                model.add(model.createResource(baseURI), LEMON.sparqlPattern, model.createResource(baseURI+"pattern_"+pattern));
                                model.add(model.createResource(baseURI+"pattern_"+pattern), OWL.hasValue, model.createLiteral(pattern));
                                model.add(model.createResource(baseURI+"pattern_"+pattern), LEMON.canonicalForm, model.createResource(baseURI+"pattern_"+pattern+"#CanonicalForm"));
                                model.add(model.createResource(baseURI+"pattern_"+pattern+"#CanonicalForm"), LEMON.writtenRep, model.createLiteral(pattern));
                                model.add(model.createResource(entry.getURI()+"#Activity"+Integer.toString(ref_counter)), PROVO.pattern, model.createResource(baseURI+"pattern_"+pattern));
                            }*/

                                
                            if (ref instanceof de.citec.sc.lemon.core.SimpleReference)
                            {
                                SimpleReference reference = (SimpleReference) ref;
                                model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)), LEMON.reference, model.createResource(reference.getURI()));
                                int synbehaviour_counter = 0;
                                for(SyntacticBehaviour synbehaviour : entry.getSenseBehaviours().get(sense)){
                                    synbehaviour_counter+=1;
                                    if (synbehaviour != null)
                                    {
                                        
                                        long timestamp = System.currentTimeMillis();
                                        for( SyntacticArgument synarc:synbehaviour.getSynArgs()){
                                            
                                            String insert_value = entry.getURI()+"#"+Long.toString(timestamp)+synarc.getValue();
                                            if(synarc.getValue().length()>13 && StringUtils.isNumeric(synarc.getValue().substring(0, 13))){
                                                insert_value = entry.getURI()+"#"+synarc.getValue().substring(13);
                                            }
                                            for(SenseArgument argument : sense.getSenseArgs()){
                                                if(argument.getValue().equals(synarc.getValue())){
                                                    model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)),model.createProperty(argument.getArgumenType()),model.createResource(insert_value));
                                                }
                                            }
//                                            model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)),LEMON.isA,model.createResource(insert_value));
                                            String syn_signature = synarc.getArgumentType()+insert_value;
                                            if(!syn_signatures.contains(syn_signature)){
                                                model.add(model.createResource(entry.getURI()), LEMON.syntacticBehaviour, model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)));
                                                model.add(model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)), RDF.type, model.createResource(synbehaviour.getFrame()));
                                                model.add(model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)),model.createProperty(synarc.getArgumentType()),model.createResource(insert_value));
                                                syn_signatures.add(syn_signature);
                                            }
                                        }

                                    }
                                }


                            }

                            if (ref instanceof de.citec.sc.lemon.core.Restriction)
                            {

                                Restriction reference = (Restriction) ref;
                                model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)), LEMON.reference, model.createResource(reference.getURI()));
                                if(reference.getValue().contains("dbpedia.org")){
                                    model.add(model.createResource(reference.getURI()), OWL.hasValue, model.createResource(reference.getValue()));
                                }
                                else model.add(model.createResource(reference.getURI()), OWL.hasValue, model.createLiteral(reference.getValue()));
                                model.add(model.createResource(reference.getURI()), OWL.onProperty, model.createResource(reference.getProperty()));
                                model.add(model.createResource(reference.getURI()), RDF.type, model.createResource("http://www.w3.org/2002/07/owl#Restriction"));
                                
                                int synbehaviour_counter = 0;
                                for(SyntacticBehaviour synbehaviour : entry.getSenseBehaviours().get(sense)){
                                    synbehaviour_counter+=1;
                                    if (synbehaviour != null)
                                    {
                                        
                                        long timestamp = System.currentTimeMillis();
                                        for( SyntacticArgument synarc:synbehaviour.getSynArgs()){
                                            String insert_value = entry.getURI()+"#"+Long.toString(timestamp)+synarc.getValue();
                                            if(synarc.getValue().length()>13 && StringUtils.isNumeric(synarc.getValue().substring(0, 13))){
                                                insert_value = entry.getURI()+"#"+synarc.getValue().substring(13);
                                            }
                                            for(SenseArgument argument : sense.getSenseArgs()){
                                                if(argument.getValue().equals(synarc.getValue())){
                                                    model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)),model.createProperty(argument.getArgumenType()),model.createResource(insert_value));
                                                }
                                            }
//                                            model.add(model.createResource(entry.getURI()+"#Sense"+Integer.toString(ref_counter)),LEMON.isA,model.createResource(insert_value));
                                            String syn_signature = synarc.getArgumentType()+insert_value;
                                            if(!syn_signatures.contains(syn_signature)){
                                                model.add(model.createResource(entry.getURI()), LEMON.syntacticBehaviour, model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)));
                                                model.add(model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)), RDF.type, model.createResource(synbehaviour.getFrame()));
                                                model.add(model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(ref_counter)+"_"+Integer.toString(synbehaviour_counter)),model.createProperty(synarc.getArgumentType()),model.createResource(insert_value));
                                                syn_signatures.add(syn_signature);
                                            }                                         }

                                    }

                                }
                            }
                   }
			
			
			
			
		}
		
		if (entry.getPOS() != null)
		{
			model.add(model.createResource(entry.getURI()), LEXINFO.partOfSpeech, model.createResource(entry.getPOS()));

		}
			
		
	}

}
