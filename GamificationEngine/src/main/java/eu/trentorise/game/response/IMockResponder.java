package eu.trentorise.game.response;

/**
 *
 * @author Luca Piras
 */
public interface IMockResponder {
    
    public SuccessResponse getPositiveResponse();
    
    public SuccessResponse getNegativeResponse();
}