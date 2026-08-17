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

import java.util.HashMap;
import java.util.Map;

/**
 * @author raman
 *
 */
public class AACTokenIntrospection extends TokenIntrospection {
	
	public AACTokenIntrospection(Map<String, Object> map) {
		super(map);
	}

	public AACTokenIntrospection() {
		super(new HashMap<>());
	}

	/**
	 * @return the aac_user_id
	 */
	public String getAac_user_id() {
		return (String)data.get("aac_user_id");
	}
	/**
	 * @return the aac_grantType
	 */
	public String getAac_grantType() {
		return (String)data.get("aac_grantType");
	}
	/**
	 * @return the aac_applicationToken
	 */
	public Boolean getAac_applicationToken() {
		return (Boolean)data.getOrDefault("aac_applicationToken", false);
	}
	/**
	 * @return the aac_am_tenant
	 */
	public String getAac_am_tenant() {
		return (String)data.get("aac_am_tenant");
	}
}
