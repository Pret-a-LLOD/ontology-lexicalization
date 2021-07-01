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
package de.citec.sc.lemon.vocabularies;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

/**
 * Class with some common static terms 
 * @author pcimiano
 */
public class LEXINFO {
	private static Model defaultModel = ModelFactory.createDefaultModel(); 
	public static Property subject = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#subject");
	public static Property object = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject");
	public static Property pobject = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject");
	public static Property copulativeArg = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg");
	public static Property possessiveAdjunct = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#possessiveAdjunct");
	public static Property partOfSpeech = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#partOfSpeech");

	public static Property verb = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
	public static Property commonNoun = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");
	public static Property adjective = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");
        public static Property preposition = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#preposition");
        
	
	public static Property transitiveFrame = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
	public static Property adjectivePPFrame = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePPFrame");
	public static Property nounPossessiveFrame = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPossessiveFrame");
	public static Property nounPredicateFrame = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPredicateFrame");
	

	
}
