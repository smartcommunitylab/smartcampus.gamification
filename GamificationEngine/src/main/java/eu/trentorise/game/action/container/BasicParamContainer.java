package eu.trentorise.game.action.container;

import eu.trentorise.game.action.model.BasicParam;


/**
 *
 * @author Luca Piras
 */
public class BasicParamContainer implements IBasicParamContainer {
    
    protected BasicParam param;

    @Override
    public BasicParam getParam() {
        return param;
    }

    @Override
    public void setParam(BasicParam param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "BasicParamContainer{" + "param=" + param + '}';
    }
}