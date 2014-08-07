package eu.trentorise.game.configuration.service;

import eu.trentorise.game.configuration.response.ImportGamifiableActionsResponse;
import eu.trentorise.game.profile.game.model.GameConfiguration;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.response.GameResponse;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockGameConfigurationManager")
public class MockGameConfigurationManager extends MockResponder implements IGameConfigurationManager {
    
    @Override
    public ImportGamifiableActionsResponse importGamifiableActions(GameConfiguration gameConfiguration) {
        ImportGamifiableActionsResponse response = new ImportGamifiableActionsResponse();
        //response.setNewGameId(MockGameProfileManager.MOCK_GAME_ID);
        return ((ImportGamifiableActionsResponse) this.buildPositiveResponse(response));
    }
    
    @Override
    public GameResponse activateDeactivatePlugin(GameConfiguration gameConfiguration) {
        return this.getPositiveResponse();
    }
}