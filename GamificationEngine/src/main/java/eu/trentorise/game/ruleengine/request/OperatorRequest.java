package eu.trentorise.game.ruleengine.request;

import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.ruleengine.model.HandSideType;

/**
 *
 * @author Luca Piras
 */
public class OperatorRequest {
    
    protected BasicParam param;
    
    protected HandSideType handSideType;

    public BasicParam getParam() {
        return param;
    }

    public void setParam(BasicParam param) {
        this.param = param;
    }

    public HandSideType getHandSideType() {
        return handSideType;
    }

    public void setHandSideType(HandSideType handSideType) {
        this.handSideType = handSideType;
    }

    @Override
    public String toString() {
        return "OperatorRequest{" + "param=" + param + ", handSideType=" + handSideType + '}';
    }
}