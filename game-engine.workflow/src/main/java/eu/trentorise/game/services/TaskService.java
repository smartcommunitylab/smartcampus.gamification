package eu.trentorise.game.services;

import org.springframework.stereotype.Service;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;

@Service
public interface TaskService {

	public void createTask(GameTask task, GameContext ctx);
}
