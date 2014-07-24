package eu.trentorise.game.ruleengine.response;

import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.ruleengine.model.Operator;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class OperatorResponse extends GameResponse {
    
    protected List<Operator> operators;

    public List<Operator> getOperators() {
        return operators;
    }

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

    @Override
    public String toString() {
        return "OperatorResponse{" + "operators=" + operators + '}';
    }
}