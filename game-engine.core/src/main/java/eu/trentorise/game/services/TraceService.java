package eu.trentorise.game.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import eu.trentorise.game.model.PlayerState;

@Service
public interface TraceService {

	public void tracePlayerMove(PlayerState old, PlayerState newOne,
			Map<String, Object> inputData, long executionTime);
}
