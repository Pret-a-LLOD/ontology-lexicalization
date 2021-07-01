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
import org.apache.jena.rdf.model.Resource;

/**
 * Class with some common static terms 
 * @author pcimiano
 */
public class LEMON {
	private static Model defaultModel = ModelFactory.createDefaultModel(); 
	public static Property lexicalizedSense = defaultModel.createProperty("http://lemon-model.net/lemon#lexicalizedSense");
	public static Property writtenRep = defaultModel.createProperty("http://lemon-model.net/lemon#writtenRep");
	public static Property language = defaultModel.createProperty("http://lemon-model.net/lemon#language");
	public static Property entry = defaultModel.createProperty("http://lemon-model.net/lemon#entry");
	public static Property sense = defaultModel.createProperty("http://lemon-model.net/lemon#sense");
	public static Property canonicalForm = defaultModel.createProperty("http://lemon-model.net/lemon#canonicalForm");
	public static Property constituent = defaultModel.createProperty("http://www.w3.org/ns/decomp#constituent");
	public static Property otherForm = defaultModel.createProperty("http://lemon-model.net/lemon#otherForm");
	public static Property identifies = defaultModel.createProperty("http://www.w3.org/ns/decomp#identifies");
	public static Property definition = defaultModel.createProperty("http://lemon-model.net/lemon#definition");
	public static Property reference = defaultModel.createProperty("http://lemon-model.net/lemon#reference");
	public static Property syntacticBehaviour = defaultModel.createProperty("http://lemon-model.net/lemon#synBehavior");
	public static Property marker = defaultModel.createProperty("http://lemon-model.net/lemon#marker");
	public static Property isA = defaultModel.createProperty("http://lemon-model.net/lemon#isA");
	public static Property objOfProp = defaultModel.createProperty("http://lemon-model.net/lemon#objOfProp");
	public static Property subjOfProp = defaultModel.createProperty("http://lemon-model.net/lemon#subjOfProp");
        public static Property sparqlPattern = defaultModel.createProperty("http://lemon-model.net/lemon#sparqlPattern");

	public static Resource Lexicon = defaultModel.createProperty("http://lemon-model.net/lemon#Lexicon");
	public static Resource LexicalEntry = defaultModel.createProperty("http://lemon-model.net/lemon#LexicalEntry");
    
     
     
       
     
}
