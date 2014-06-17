package eu.trentorise.game.profile.game.service;

import eu.trentorise.game.profile.game.model.GameConfiguration;
import eu.trentorise.game.profile.game.response.NewGameResponse;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.response.GameResponse;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockGameProfileManager")
public class MockGameProfileManager extends MockResponder implements IGameProfileManager {

    @Override
    public NewGameResponse newGame(Object object) {
        NewGameResponse response = new NewGameResponse();
        response.setNewGameId(135);
        return ((NewGameResponse) this.buildPositiveResponse(response));
    }
    
    @Override
    public GameResponse activateDeactivatePlugin(GameConfiguration gameConfiguration) {
        return this.getPositiveResponse();
    }
}