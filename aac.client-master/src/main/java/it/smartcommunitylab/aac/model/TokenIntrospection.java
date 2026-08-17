/*******************************************************************************
 * Copyright 2015 Fondazione Bruno Kessler
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

/**
 * ITEF RFC7662 Token Introspection model
 * @author raman
 *
 */
public class TokenIntrospection {

	protected Map<String, Object> data;
	
	public TokenIntrospection(Map<String, Object> map) {
		this.data = map;
		this.data.put("scope", Arrays.asList(((String)data.getOrDefault("scope", "")).split(" ")).stream().map(s-> s.trim()).collect(Collectors.toSet()));
	}
	
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return (Boolean)data.getOrDefault("active", false);
	}
	/**
	 * @return the scope
	 */
	@SuppressWarnings("unchecked")
	public Set<String> getScope() {
		return (Set<String>) data.getOrDefault("scope", Sets.newLinkedHashSet());
	}
	/**
	 * @return the client_id
	 */
	public String getClient_id() {
		return (String)data.get("client_id");
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return (String)data.get("username");
	}
	/**
	 * @return the token_type
	 */
	public String getToken_type() {
		return (String)data.get("token_type");
	}
	/**
	 * @return the sub
	 */
	public String getSub() {
		return (String)data.get("sub");
	}
	/**
	 * @return the iss
	 */
	public String getIss() {
		return (String)data.get("iss");
	}
	/**
	 * @return the jti
	 */
	public String getJti() {
		return (String)data.get("jti");
	}
	/**
	 * @return the exp
	 */
	public Integer getExp() {
		return (Integer)data.get("exp");
	}
	/**
	 * @return the iat
	 */
	public Integer getIat() {
		return (Integer)data.get("iat");
	}
	/**
	 * @return the nbf
	 */
	public Integer getNbf() {
		return (Integer)data.get("nbf");
	}
	/**
	 * @return the aud
	 */
	@SuppressWarnings("unchecked")
	public String[] getAud() {
		return 
				  data.get("aud") == null 
				? new String[]{}
				: (data.get("aud") instanceof List)
				? ((List<String>) data.get("aud")).toArray(new String[0]) 
				: (data.get("aud") instanceof String[])
				? (String[])data.get("aud")
				: new String[] {data.get("aud").toString()};
	}
}
