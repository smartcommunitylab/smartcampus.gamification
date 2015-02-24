package eu.trentorise.game.services;

import java.util.List;

import org.springframework.stereotype.Service;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;

@Service
public interface TaskService {

	public void createTask(GameTask task, GameContext ctx);

	public boolean destroyTask(GameTask task, String gameId);

	public String saveData(String gameId, String taskName, Object data);

	public List<Object> readData(String gameId, String taskName);
}
