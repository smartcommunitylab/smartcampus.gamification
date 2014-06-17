package eu.trentorise.game.response;

/**
 *
 * @author Luca Piras
 */
public class GameResponse {
    
    protected Boolean success;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "GameResponse{" + "success=" + success + '}';
    }
}