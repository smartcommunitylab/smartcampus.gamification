package eu.trentorise.game.services;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface Workflow {

	public void apply(String gameId, String actionId, String userId,
			Map<String, Object> data);
}
