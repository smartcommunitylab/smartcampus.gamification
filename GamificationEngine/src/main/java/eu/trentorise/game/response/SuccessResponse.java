package eu.trentorise.game.response;

/**
 *
 * @author Luca Piras
 */
public class SuccessResponse {
    
    protected Boolean success;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "SuccessResponse{" + "success=" + success + '}';
    }
}