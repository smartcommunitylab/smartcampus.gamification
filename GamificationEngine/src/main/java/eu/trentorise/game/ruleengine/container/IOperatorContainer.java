package eu.trentorise.game.ruleengine.container;

import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.ruleengine.model.HandSideType;

/**
 *
 * @author Luca Piras
 */
public interface IOperatorContainer {

    public BasicParam getParam();
    public void setParam(BasicParam param);

    public HandSideType getHandSideType();
    public void setHandSideType(HandSideType handSideType);
}