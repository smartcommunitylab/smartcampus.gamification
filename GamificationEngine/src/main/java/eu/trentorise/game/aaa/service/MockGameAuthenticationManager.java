package eu.trentorise.game.aaa.service;

import eu.trentorise.game.aaa.request.GameAuthenticationRequest;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.response.GameResponse;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockGameAuthenticationManager")
public class MockGameAuthenticationManager extends MockResponder implements IGameAuthenticationManager {

    @Override
    public GameResponse authenticate(GameAuthenticationRequest gameAuthentication) {
        return this.getPositiveResponse();
    }
}