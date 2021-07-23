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
package de.citec.sc.lemon.core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @author swalter
 */
public class Lexicon {

	//HashMap<String,List<LexicalEntry>> map;
	
	List<LexicalEntry> entries;
	
	Set<Reference> references;
	
	String baseURI = "";
	
	public Lexicon()
	{
		entries = new ArrayList<LexicalEntry>();
		
//		map = new HashMap<String,List<LexicalEntry>>();
		
		references = new HashSet<Reference>();
                
                this.baseURI = "http://dblexipedia.org/lexicon/";
	}
	
	public Lexicon(String baseURI)
	{
		entries = new ArrayList<LexicalEntry>();
		
//		map = new HashMap<String,List<LexicalEntry>>();
		
		references = new HashSet<Reference>();
		
		this.baseURI = baseURI;
	}

	
        public void addLexicon(Lexicon lex){
            lex.getEntries().stream().forEach((e) -> {
                this.addEntry(e);
            });
        }
        
	public void addEntry(LexicalEntry entry)
	{
		
		if(!entries.contains(entry))
		{
			entries.add(entry);
		
		}
		else
		{   
                        /*
                    This is the entry from the list entries, which is updated
                    */
                        //System.out.println("Update entry");
                        LexicalEntry containedEntry;
	
			containedEntry = getLexicalEntry(entry);
                        //if entry of URI is the same, but different forms appear, add alternative forms
                        if(!containedEntry.getCanonicalForm().equals(entry.getCanonicalForm())){
                            containedEntry.addAlternativeForms(entry.getCanonicalForm());
                        }
                        if(!entry.getAlternativeForms().isEmpty()) containedEntry.addAlternativeFormsAll(entry.getAlternativeForms());
                        
                        HashMap<Sense, HashSet<SyntacticBehaviour>> senseBehaviours = entry.getSenseBehaviours();
                        senseBehaviours.keySet().stream().map((sense) -> {
                            HashSet<SyntacticBehaviour> behaviours = senseBehaviours.get(sense);
                            containedEntry.addAllSyntacticBehaviour(behaviours, sense);
                        return sense;
                        }).forEach((sense) -> {
                            /*
                            Update Provenance
                            */
                            //recently deleted
                            //Provenance provenance = entry.getProvenance(sense);
                            //containedEntry.addProvenance(provenance, sense);
                        });

                        
                        
                        
			
		}
	
		if (entry.getSenseBehaviours() != null)
                    entry.getReferences().stream().forEach((reference) -> {
                        references.add(reference);
                });
			
	}

        public String getBaseURI() 
        {
               return baseURI;
        }
        
        public void setBaseURI(String baseURI){
            this.baseURI = baseURI;
        }
        
	public List<LexicalEntry> getEntries()
	{
		return entries;
	}
        
        public List<LexicalEntry> getEntries(Language language)
	{
		List<LexicalEntry> entries_tmp = new ArrayList<LexicalEntry>();
                for(LexicalEntry e : getEntries()){
                    try{
                        if(e.getLanguage().equals(language)) entries_tmp.add(e);
                    }
                    catch(Exception exp){
                        System.err.println("Entry: "+e.toString()+"does NOT contain any language");
                    }
                }
                return entries_tmp;
	}
	
	public List<LexicalEntry> getEntriesWithCanonicalForm(String canonicalForm)
	{
            List<LexicalEntry> results = new ArrayList<>();
            for(LexicalEntry entry: entries){
                if(entry.getCanonicalForm().equals(canonicalForm)) results.add(entry);
            }
            
            return results;
	}
	
	public int size() {

		return entries.size();
	}
	
	public Set<Reference> getReferences()
	{
		Set<Reference> local_references = new HashSet<Reference>();
		
                entries.stream().forEach((entry) -> {   
                    entry.getSenseBehaviours().keySet().stream().forEach((sense) -> {
                        local_references.add(sense.getReference());
                    });
            });
		
		return local_references;
	}
	

	private LexicalEntry createNewEntry(String canonicalForm, Language language) {
		
		LexicalEntry entry = new LexicalEntry(language);
		
		entry.setCanonicalForm(canonicalForm);
		
		addEntry(entry);
		
		entry.setURI(baseURI+"LexicalEntry_"+entries.size()+"_"+canonicalForm);
		
		return entry;
	
	}

	public boolean contains(LexicalEntry entry)
	{
		return entries.contains(entry);
	}
	
        private LexicalEntry getLexicalEntry(LexicalEntry entry){
            String uri = entry.getURI();
            for(LexicalEntry containedEntry : entries){
                /// if URI are euqal, then we assume the whole entry is the same (but maybe contains different senses)
                if(containedEntry.getURI().equals(uri)) return containedEntry;
            }
            return null;
        }
                
	
	public Iterator<LexicalEntry> iterator()
	{
		return entries.iterator();
	}
	
        @Override
	public String toString()
	{
		String string = "";
		
                string = entries.stream().map((entry) -> entry.toString() + "\n\n").reduce(string, String::concat);
		
		return string;
	}
        

	public List<LexicalEntry> getEntriesForReference(String input_ref) {
		
		List<LexicalEntry> Local_entries = new ArrayList<LexicalEntry>();
		
		for (LexicalEntry entry: this.entries)
		{
                    for(Reference ref: entry.getReferences()){
                        if (ref.toString().equals(input_ref)) {
                            Local_entries.add(entry);
                            break;
                        }
                    }
			
		}
		
		return Local_entries;
	}
        
        public List<Preposition> getPrepositions(){
            
            List<Preposition> prepositions = this.entries.stream()
                    .filter(e->e.getPreposition()!=null)
                    .map((LexicalEntry e)->{return e.getPreposition();})
                    .collect(Collectors.toList());
            return prepositions;
        }
        
        

}

