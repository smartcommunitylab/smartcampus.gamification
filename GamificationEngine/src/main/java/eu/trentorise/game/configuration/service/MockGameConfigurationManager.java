package eu.trentorise.game.configuration.service;

import eu.trentorise.game.profile.game.model.GameConfiguration;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.response.GameResponse;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Deprecated
@Service("mockGameConfigurationManager")
public class MockGameConfigurationManager extends MockResponder implements IGameConfigurationManager {
    
    @Override
    public GameResponse activateDeactivatePlugin(GameConfiguration gameConfiguration) {
        return this.getPositiveResponse();
    }
}