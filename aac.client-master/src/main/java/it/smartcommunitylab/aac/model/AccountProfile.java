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

package it.smartcommunitylab.aac.model;

import java.util.HashMap;
import java.util.Map;

/**
 * User registration information (different account of the registered user)
 * @author raman
 *
 */
public class AccountProfile extends BasicProfile {

	private Map<String,Map<String,String>> attributes;
	
	public AccountProfile() {
		attributes = new HashMap<String, Map<String,String>>();
	}

	public Map<String,Map<String,String>> getAccounts() {
		return attributes;
	}
	
	/**
	 * Return all the attributes of the specified account
	 * @param account
	 * @return
	 */
	public Map<String,String> getAccountAttributes(String account) {
		return attributes.get(account);
	}
	
	/**
	 * Return the value for the specified attribute of the specified account.
	 * @param account
	 * @param attribute
	 * @return
	 */
	public String getAttribute(String account, String attribute) {
		if (!attributes.containsKey(account)) return null;
		return attributes.get(account).get(attribute);
	}
	
	/**
	 * Add attribute
	 * @param account
	 * @param attribute
	 * @param value
	 */
	public void addAttribute(String account, String attribute, String value) {
		if (account != null && attribute != null) {
			if (attributes.get(account) == null) {
				attributes.put(account, new HashMap<String, String>());
			}
			attributes.get(account).put(attribute, value);
		}
	}
}
