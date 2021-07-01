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

import java.util.Objects;

/**
 *
 * @author swalter
 */
public class Sentence {
    String sentence;
    String subjOfProp;
    String objOfProp;
    String subjOfProp_uri;
    String objOfProp_uri;
    
    public Sentence(String sentence, String subjOfProp, String objOfProp){
        this.sentence = sentence;
        this.subjOfProp = subjOfProp;
        this.objOfProp = objOfProp;
    }
    
    public String getSentence() {
        return sentence;
    }

    public String getSubjOfProp() {
        return subjOfProp;
    }

    public String getObjOfProp() {
        return objOfProp;
    }
    public String getSubjOfProp_uri() {
        return subjOfProp_uri;
    }

    public void setSubjOfProp_uri(String subjOfProp_uri) {
        this.subjOfProp_uri = subjOfProp_uri;
    }

    public String getObjOfProp_uri() {
        return objOfProp_uri;
    }

    public void setObjOfProp_uri(String objOfProp_uri) {
        this.objOfProp_uri = objOfProp_uri;
    }
    
    @Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
                Sentence input = (Sentence) obj;
		if (!getSubjOfProp().equals(input.getSubjOfProp()))
			return false;
		if (!getObjOfProp().equals(input.getObjOfProp()))
			return false;
		return true;
	}

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.sentence);
        hash = 67 * hash + Objects.hashCode(this.subjOfProp);
        hash = 67 * hash + Objects.hashCode(this.objOfProp);
        return hash;
    }
    
    @Override
    public String toString(){
        return sentence+";"+subjOfProp+";"+objOfProp;
    }


    
    
}
