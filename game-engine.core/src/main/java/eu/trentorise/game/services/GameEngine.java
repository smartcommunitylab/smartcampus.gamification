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
