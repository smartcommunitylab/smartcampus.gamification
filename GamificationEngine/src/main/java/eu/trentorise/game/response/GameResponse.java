package eu.trentorise.game.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Luca Piras
 */
//Necessary for the getCustomizedPluginListService Test
@JsonIgnoreProperties(ignoreUnknown = true)
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