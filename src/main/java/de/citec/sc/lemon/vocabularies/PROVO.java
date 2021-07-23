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
public class PROVO {
	private static Model defaultModel = ModelFactory.createDefaultModel(); 
	public static Property associatedWith = defaultModel.createProperty("http://www.w3.org/ns/prov#associatedWith");
	public static Property generatedBy = defaultModel.createProperty("http://www.w3.org/ns/prov#generatedBy");
	public static Property startedAtTime = defaultModel.createProperty("http://www.w3.org/ns/prov#startedAtTime");
	public static Property endedatTime = defaultModel.createProperty("http://www.w3.org/ns/prov#endedAtTime");
        public static Property confidence = defaultModel.createProperty("http://www.w3.org/ns/prov#confidence");
	public static Property frequency = defaultModel.createProperty("http://www.w3.org/ns/prov#frequency");
        public static Property pattern = defaultModel.createProperty("http://www.w3.org/ns/prov#pattern");
        public static Property query = defaultModel.createProperty("http://www.w3.org/ns/prov#query");
        public static Property sentence = defaultModel.createProperty("http://www.w3.org/ns/prov#sentence");

	
	//public static Resource Activity = defaultModel.createProperty("http://www.w3.org/ns/prov#Activity");
	public static Resource Entity = defaultModel.createProperty("http://www.w3.org/ns/prov#Entity");
	public static Resource Agent = defaultModel.createProperty("http://www.w3.org/ns/prov#Agent");
    
     
     
       
     
}
