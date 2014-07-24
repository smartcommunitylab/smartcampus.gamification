package eu.trentorise.game.ruleengine.container;

import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.ruleengine.model.HandSideType;

/**
 *
 * @author Luca Piras
 */
public class OperatorContainer implements IOperatorContainer {
    
    protected BasicParam param;
    
    protected HandSideType handSideType;

    @Override
    public BasicParam getParam() {
        return param;
    }

    @Override
    public void setParam(BasicParam param) {
        this.param = param;
    }

    @Override
    public HandSideType getHandSideType() {
        return handSideType;
    }

    @Override
    public void setHandSideType(HandSideType handSideType) {
        this.handSideType = handSideType;
    }

    @Override
    public String toString() {
        return "OperatorContainer{" + "param=" + param + ", handSideType=" + handSideType + '}';
    }
}