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

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author swalter
 */
public class Sense {

	Set<SenseArgument> senseArgs;
	Reference Reference;
	
	public Sense()
	{
		senseArgs = new HashSet<SenseArgument>();
	}
	
	public Set<SenseArgument> getSenseArgs() {
		return senseArgs;
	}
	public void setSenseArgs(Set<SenseArgument> senseArgs) {
		this.senseArgs = senseArgs;
	}
	
	public void addSenseArg(SenseArgument senseArg)
	{
		senseArgs.add(senseArg);
	}
	
	@Override
	public String toString() {
		
		String string = "";
		
		string += "Reference: "+Reference+"\n";
		
		for (SenseArgument arg: senseArgs)
		{
			string += "\t SenseArg: "+arg.getArgumenType()+"\n";
		}
		
		return string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((Reference == null) ? 0 : Reference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
            
                if(obj== null) return false;
                else{
                    Sense other = (Sense) obj;
                    if(other.getReference()==null||Reference == null){
                        return false;
                    }
                    else{
                        if(other.getReference().getURI().equals(Reference.getURI())){
                            if(senseArgs == null||other.senseArgs==null){
                                return false;
                            }
                            else{
                                if(senseArgs.equals(other.senseArgs)){
                                    return true;
                                }
                                else{
                                    return false;
                                }
                            }
                        }
                    }
                }
                return false;
//		 System.out.print("I am in equals (Sense)\n");
//		
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		Sense other = (Sense) obj;
//		if (Reference == null) {
//			if (other.Reference != null)
//				return false;
//		} else if (!Reference.equals(other.Reference))
//			{
//				 System.out.print("Reference is different\n");
//				return false;
//			}
//		if (senseArgs == null) {
//			if (other.senseArgs != null)
//				return false;
//		} 
//		else if (!senseArgs.equals(other.senseArgs))
//			{
//				 System.out.print("Sense args are different\n");
//				 System.out.println(senseArgs);
//				 System.out.println(other.senseArgs);
//				return false;
//			}
//		return true;
	}

	public Reference getReference() {
		return Reference;
	}
	public void setReference(Reference reference) {
		Reference = reference;
	}
	
}
