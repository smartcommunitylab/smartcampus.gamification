package eu.trentorise.game.profile.game.service;

import eu.trentorise.game.profile.game.container.INewGameContainer;
import eu.trentorise.game.profile.game.response.NewGameResponse;
import eu.trentorise.game.response.MockResponder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockGameProfileManager")
public class MockGameProfileManager extends MockResponder implements IGameProfileManager {

    @Override
    public NewGameResponse newGame(INewGameContainer container) {
        NewGameResponse response = new NewGameResponse();
        response.setGame(container.getGame());
        response.getGame().setId(135);
        
        return ((NewGameResponse) this.buildPositiveResponse(response));
    }
}