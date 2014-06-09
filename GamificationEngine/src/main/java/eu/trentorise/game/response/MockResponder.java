package eu.trentorise.game.response;

/**
 *
 * @author Luca Piras
 */
public class MockResponder implements IMockResponder {
    
    @Override
    public SuccessResponse getPositiveResponse() {
        return this.getResponse(Boolean.TRUE);
    }

    @Override
    public SuccessResponse getNegativeResponse() {
        return this.getResponse(Boolean.FALSE);
    }
    
    protected SuccessResponse getResponse(Boolean success) {
        SuccessResponse response = new SuccessResponse();
        
        response.setSuccess(success);
        
        return response;
    }
}