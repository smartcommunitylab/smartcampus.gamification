package eu.trentorise.game.action.response;

import eu.trentorise.game.response.GameResponse;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class OperatorsResponse extends GameResponse {
    
    protected List<String> operators;

    public List<String> getOperators() {
        return operators;
    }

    public void setOperators(List<String> operators) {
        this.operators = operators;
    }

    @Override
    public String toString() {
        return "OperatorsResponse{" + "operators=" + operators + '}';
    }
}