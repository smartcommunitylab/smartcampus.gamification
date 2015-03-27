/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
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
 */

package eu.trentorise.game.repo;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericObjectPersistence {
	private Map<String, Object> obj;
	private String type;

	public GenericObjectPersistence(Object obj) {
		ObjectMapper mapper = new ObjectMapper();

		this.obj = mapper.convertValue(obj, Map.class);
		this.type = obj.getClass().getCanonicalName();
	}

	public GenericObjectPersistence() {
	}

	public Map<String, Object> getObj() {
		return obj;
	}

	public void setObj(Map<String, Object> concept) {
		this.obj = concept;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
