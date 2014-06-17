package eu.trentorise.game.response;

/**
 *
 * @author Luca Piras
 */
public class MockResponder implements IMockResponder {
    
    @Override
    public GameResponse getPositiveResponse() {
        return this.getResponse(null, Boolean.TRUE);
    }
    
    @Override
    public GameResponse buildPositiveResponse(GameResponse response) {
        return this.getResponse(response, Boolean.TRUE);
    }

    @Override
    public GameResponse getNegativeResponse() {
        return this.getResponse(null, Boolean.FALSE);
    }
    
    @Override
    public GameResponse buildNegativeResponse(GameResponse response) {
        return this.getResponse(response, Boolean.FALSE);
    }
    
    protected GameResponse getResponse(GameResponse response, Boolean success) {
        if (null == response) {
            response = new GameResponse();
        }
        
        response.setSuccess(success);
        
        return response;
    }
}