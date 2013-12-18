package eu.trentorise.game.service;

import eu.trentorise.game.model.player.Player;
import eu.trentorise.game.response.SuccessResponse;
import org.springframework.stereotype.Service;


@Service("mockPlayerManager")
public class MockPlayerManager implements IPlayerManager {

    @Override
    public SuccessResponse add(Player player) {
        SuccessResponse response = new SuccessResponse();
        
        response.setSuccess(Boolean.TRUE);
        
        return response;
    }
}