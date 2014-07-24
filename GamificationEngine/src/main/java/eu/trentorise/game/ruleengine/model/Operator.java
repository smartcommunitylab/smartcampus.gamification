package eu.trentorise.game.ruleengine.model;

/**
 *
 * @author Luca Piras
 */
public class Operator {
    
    protected String Symbol;

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String Symbol) {
        this.Symbol = Symbol;
    }

    @Override
    public String toString() {
        return "RuleOperator{" + "Symbol=" + Symbol + '}';
    }
}