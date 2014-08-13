package eu.trentorise.game.action.response;

import eu.trentorise.game.action.model.Param;

/**
 *
 * @author Luca Piras
 */
public class ParamResponse {
    
    protected Param param;

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "ParamResponse{" + "param=" + param + '}';
    }
}