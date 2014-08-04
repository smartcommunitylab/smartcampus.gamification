package eu.trentorise.game.response;

/**
 *
 * @author Luca Piras
 */
@Deprecated
public interface IMockResponder {
    
    public GameResponse getPositiveResponse();
    
    public GameResponse buildPositiveResponse(GameResponse response);
    
    public GameResponse getNegativeResponse();
    
    public GameResponse buildNegativeResponse(GameResponse response);
}