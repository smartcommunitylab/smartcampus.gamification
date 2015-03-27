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

package eu.trentorise.game.model;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskDataConverter {

	private ObjectMapper mapper;

	public TaskDataConverter() {
		mapper = new ObjectMapper();
	}

	public <T> T convert(Object o, Class<T> classtype) {
		return mapper.convertValue(o, classtype);
	}

	public <T> List<T> convert(List<Object> o, Class<T> classtype) {
		return mapper.convertValue(o, new TypeReference<List<T>>() {
		});
	}

}
