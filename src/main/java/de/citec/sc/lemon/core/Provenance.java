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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author swalter
 */

public class Provenance {

    String Agent;

    Date StartedAtTime;
    Date EndedAtTime;

    Double Confidence;

    String POS;


    Integer Frequency;
    HashSet<String> patternset = new HashSet<String>();

    Integer Annotation = 0;

    Double OveralLabelRatio = 0.0;

    /*
    iterates over all entries and sums up all frequencys for a certain property.
    This ratio is then the frequency of the current provenance, divided by the number from above
    */
    Double OverallPropertyEntryRatio = 0.0; 

    List<Sentence> Sentences = new ArrayList<>();

    public HashSet<String> getPatternset() {
        return patternset;
    }
    
    
    public Double getAvaerage_lenght() {
        if(Sentences.isEmpty()) return 0.0;
        else{
            double avg = 0.0;
            List<Sentence> sentences = getShortestSentences(5);
            for(Sentence sentence:sentences)avg+=sentence.getSentence().length();
            avg = avg/sentences.size();

            return avg;
        }
        
    }

    public void setPatternset(HashSet<String> pattern) {
        this.patternset = pattern;
    }

    public void addPattern(String pattern) {
        this.patternset.add(pattern);
    }
    
    public void addAllPattern(HashSet<String> patternlist) {
        this.patternset.addAll(patternlist);
    }
    
        public Integer getFrequency() {
            return Frequency;
        }

        public void setFrequency(Integer frequency) {
            this.Frequency = frequency;
        }
        
        public void increaseFrequency(Integer frequency){
            this.Frequency += frequency;
        }

	public String getAgent()
	{
		return Agent;
	}
	
	public void setAgent(String agent)
	{
		Agent = agent;
	}
	
	public Double getConfidence()
	{
		return Confidence;
	}
	
	public void setConfidence(Double confidence)
	{
		Confidence = confidence;
	}
	
	public void setStartedAtTime(Date date)
	{
		StartedAtTime = date;
	}
	
	public void setEndedAtTime(Date date)
	{
		EndedAtTime = date;
	}
	
	public Date getStartedAtTime()
	{
		return StartedAtTime;
	}
	
	public Date getEndedAtTime()
	{
		return EndedAtTime;
	}
        
        public List<Sentence> getSentences() {
		return Sentences;
	}


	public void addSentences(List<Sentence> sentences) {
		Sentences.addAll(sentences);
		
	}


	public void setSentences(List<Sentence> sentences) {
		Sentences = sentences;
		
	}
        
        public void addSentence(Sentence sentence) {
		Sentences.add(sentence);
		
	}
        
        /**
         * Returns the k longest sentences as a list
         * @param k number of sentences
         * @return 
         */
        public List<Sentence> getShortestSentences(int k){
            List<Sentence> sentences = new ArrayList<>();
            Map<Sentence, Integer> map = new HashMap<Sentence, Integer>();
            Sentences.stream().forEach((sentence) -> { map.put(sentence, sentence.getSentence().length());});
            Map<Sentence,Integer> sortedMap = sortByComparator(map);
            int counter = 0;
            for(Sentence key : sortedMap.keySet()){
                if(counter<k)sentences.add(key);
                counter++;
            }
            return sentences;
        }
        
        //http://www.mkyong.com/java/how-to-sort-a-map-in-java/
        private static Map<Sentence, Integer> sortByComparator(Map<Sentence, Integer> unsortMap) {
 
		// Convert Map to List
		List<Map.Entry<Sentence, Integer>> list = 
			new LinkedList<Map.Entry<Sentence, Integer>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Sentence, Integer>>() {
			public int compare(Map.Entry<Sentence, Integer> o1,
                                           Map.Entry<Sentence, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<Sentence, Integer> sortedMap = new LinkedHashMap<Sentence, Integer>();
		for (Iterator<Map.Entry<Sentence, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Sentence, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
        
        public Integer getAnnotation() {
            return Annotation;
        }

        public void setAnnotation(Integer Annotation) {
            this.Annotation = Annotation;
        }
        
        public Double getOveralLabelRatio() {
            return OveralLabelRatio;
        }

        public void setOveralLabelRatio(Double OveralLabelRatio) {
            this.OveralLabelRatio = OveralLabelRatio;
        }
        public Double getOverallPropertyEntryRatio() {
            return OverallPropertyEntryRatio;
        }

        public void setOverallPropertyEntryRatio(Double OverallPropertyEntryRatio) {
            this.OverallPropertyEntryRatio = OverallPropertyEntryRatio;
        }
        public String getPOS() {
            return POS;
        }

        public void setPOS(String POS) {
            this.POS = POS;
        }
        
    
	
}
