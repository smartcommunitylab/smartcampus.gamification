package eu.trentorise.game.application.response;

import eu.trentorise.game.application.model.Param;
import eu.trentorise.game.response.GameResponse;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class ParamResponse extends GameResponse {
    
    protected List<Param> params;

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "ParamResponse{" + "params=" + params + '}';
    }
}