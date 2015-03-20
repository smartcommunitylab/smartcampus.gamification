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
			boolean ownerCond = game.getOwner() == null
					|| game.getOwner().equals(user);
			boolean gameCond = game == null || ownerCond;
			return user != null && gameCond;
		}

		return true;
	}
}
