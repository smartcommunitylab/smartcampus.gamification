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

package eu.trentorise.game.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.Game;

@Component
public class PermissionManager {

	@Autowired
	private GameManager gameManager;

	public boolean checkGamePermission(String user, String gameId) {
		if (gameId != null) {
			Game game = gameManager.loadGameDefinitionById(gameId);
			boolean gameCond = game == null || game.getOwner() == null
					|| game.getOwner().equals(user);
			return user != null && gameCond;
		}

		return true;
	}
}
