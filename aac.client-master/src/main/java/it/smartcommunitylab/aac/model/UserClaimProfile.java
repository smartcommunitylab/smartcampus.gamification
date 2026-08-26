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

import java.util.Map;

/**
 * @author raman
 *
 */
public class UserClaimProfile {

	private String userId, username;
	Map<String, Object> claims;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Map<String, Object> getClaims() {
		return claims;
	}
	public void setClaims(Map<String, Object> claims) {
		this.claims = claims;
	}
	public UserClaimProfile() {
		super();
	}
	public UserClaimProfile(String userId, String username) {
		super();
		this.userId = userId;
		this.username = username;
	}

}
