package eu.trentorise.game.response;

/**
 *
 * @author Luca Piras
 */
public interface IMockResponder {
    
    public GameResponse getPositiveResponse();
    
    public GameResponse buildPositiveResponse(GameResponse response);
    
    public GameResponse getNegativeResponse();
    
    public GameResponse buildNegativeResponse(GameResponse response);
}