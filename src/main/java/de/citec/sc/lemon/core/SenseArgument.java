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

/**
 * 
 * @author swalter
 */
public class SenseArgument {

	String ArgumentType;
	String Value;
	
	/**
         * Sets the argument Type to an value (identifier)
         * @param argumentType
         * @param value Has to be a unique identifier
         */
	public SenseArgument(String argumentType, String value) {
		ArgumentType = argumentType;
		Value = value;
	}

	public String getArgumenType()
	{
		return ArgumentType;
	}
	
        /**
         * Gets the identifier for a given SenseArgument. If set correctly, identifier is unique in the world of one lexical entry.
         * @return identifier
         */
	public String getValue()
	{
		return Value;
	}

	@Override
	public String toString() {
		return "SenseArgument [ArgumentType=" + ArgumentType + ", Value="
				+ Value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
                /**
                 * TODO: Why prime * result ? The result(without the addition) is always 31; with the additional term the result is a multiple of 31
                 */
		result = prime * result
				+ ((ArgumentType == null) ? 0 : ArgumentType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SenseArgument other = (SenseArgument) obj;
		if (ArgumentType == null) {
			if (other.ArgumentType != null)
				return false;
		} else if (!ArgumentType.equals(other.ArgumentType))
			return false;
		
		return true;
	}
	
	
	
	
	
}
