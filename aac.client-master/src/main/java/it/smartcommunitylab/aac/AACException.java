/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package it.smartcommunitylab.aac;

/**
 * Exception thrown by {@link AACService}
 *
 */
public class AACException extends Exception {

	private static final long serialVersionUID = -6682965816616260202L;

	public AACException() {
		super();
	}

	public AACException(String message, Throwable cause) {
		super(message, cause);
	}

	public AACException(String message) {
		super(message);
	}

	public AACException(Throwable cause) {
		super(cause);
	}

}
