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

package eu.trentorise.game.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import eu.trentorise.game.model.PlayerState;

@Service
public interface GameEngine {

	public PlayerState execute(String gameId, PlayerState state, String action,
			Map<String, Object> data);

	/**
	 * Rule syntax validation
	 * 
	 * 
	 * @param content
	 *            the rule content
	 * @return the list of syntax errors, or an empty list if validation gone
	 *         fine
	 */
	public List<String> validateRule(String content);
}
