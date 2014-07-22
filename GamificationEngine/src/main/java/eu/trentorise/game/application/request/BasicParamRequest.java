package eu.trentorise.game.application.request;

import eu.trentorise.game.application.model.BasicParam;

/**
 *
 * @author Luca Piras
 */
public class BasicParamRequest {
    
    protected BasicParam param;

    public BasicParam getParam() {
        return param;
    }

    public void setParam(BasicParam param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "BasicParamRequest{" + "param=" + param + '}';
    }
}