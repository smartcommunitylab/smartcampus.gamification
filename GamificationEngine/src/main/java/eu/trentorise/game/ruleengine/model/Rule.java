package eu.trentorise.game.ruleengine.model;

import eu.trentorise.game.plugin.model.GamificationPlugin;

/**
 *
 * @author Luca Piras
 */
public class Rule {
    
    protected Integer id;
    protected RuleTemplate ruleTemplate;
    protected GamificationPlugin gamificationPlugin;
    
    protected Operator lhsOperator;
    protected Operator rhsOperator;
    
    protected String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RuleTemplate getRuleTemplate() {
        return ruleTemplate;
    }

    public void setRuleTemplate(RuleTemplate ruleTemplate) {
        this.ruleTemplate = ruleTemplate;
    }

    public GamificationPlugin getGamificationPlugin() {
        return gamificationPlugin;
    }

    public void setGamificationPlugin(GamificationPlugin gamificationPlugin) {
        this.gamificationPlugin = gamificationPlugin;
    }

    public Operator getLhsOperator() {
        return lhsOperator;
    }

    public void setLhsOperator(Operator lhsOperator) {
        this.lhsOperator = lhsOperator;
    }

    public Operator getRhsOperator() {
        return rhsOperator;
    }

    public void setRhsOperator(Operator rhsOperator) {
        this.rhsOperator = rhsOperator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Rule{" + "id=" + id + ", ruleTemplate=" + ruleTemplate + ", gamificationPlugin=" + gamificationPlugin + ", lhsOperator=" + lhsOperator + ", rhsOperator=" + rhsOperator + ", content=" + content + '}';
    }
}