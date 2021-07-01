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

public class SyntacticBehaviour {

	String Frame;
	
	Set<SyntacticArgument> synArgs;

	public String getFrame() {
		return Frame;
	}

	public SyntacticBehaviour() {
		synArgs = new HashSet<SyntacticArgument>();
	}

	public void setFrame(String frame) {
		Frame = frame;
	}

	public Set<SyntacticArgument> getSynArgs() {
		return synArgs;
	}

	public void setSynArgs(Set<SyntacticArgument> synArgs) {
		this.synArgs = synArgs;
	}
	
	public void add(SyntacticArgument synArg)
	{
		synArgs.add(synArg);
	}
	
	@Override
	public String toString() {
		String string = "";
		
		string += "Frame:" +Frame+ "\n";
		
		for (SyntacticArgument arg: synArgs)
		{
			string += "\t Syntactic Argument: "+arg.getArgumentType() + " ("+arg.getPreposition()+")\n";
		}
		
		return string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Frame == null) ? 0 : Frame.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		// System.out.print("I am in equals (Syntactic Behaviour)\n");
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SyntacticBehaviour other = (SyntacticBehaviour) obj;
		if (Frame == null) {
			if (other.Frame != null)
				return false;
		} else if (!Frame.equals(other.Frame))
			{
				// System.out.print("Frames are different!\n");
				return false;
			}
		if (synArgs == null) {
			if (other.synArgs != null)
				return false;
		} 
			else if (!synArgs.equals(other.synArgs))
			{
				// System.out.print("synArgs are different!\n");
				return false;
			}
			
		return true;
	}
	
	
}
