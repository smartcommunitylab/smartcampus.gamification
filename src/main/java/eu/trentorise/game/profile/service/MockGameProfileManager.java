package eu.trentorise.game.profile.service;

import eu.trentorise.game.profile.model.GameConfiguration;
import eu.trentorise.game.response.SuccessResponse;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockGameProfileManager")
public class MockGameProfileManager implements IGameProfileManager {

    @Override
    public SuccessResponse activateDeactivatePlugin(GameConfiguration gameConfiguration) {
        SuccessResponse response = new SuccessResponse();
        
        response.setSuccess(Boolean.TRUE);
        
        return response;
    }
}