package eu.trentorise.game.ruleengine.model;

import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.Reward;

/**
 *
 * @author Luca Piras
 */
public class Rule {
    
    //ids and/or foreignkeys
    protected Integer id;
    protected RuleTemplate ruleTemplate;
    
    //trigger
    protected Action action;
    
    //leftHandSide
    protected BasicParam lhsBasicParam;
    protected Operator lhsOperator;
    protected String lhsOperand;
    
    //customizedPluginInvolved
    protected CustomizedPlugin customizedPlugin;
    
    //rightHandSide
    protected Operator rhsPluginOperator;
    protected String rhsValue;
    protected BasicParam rhsBasicParam;
    protected Operator rhsOperator;
    protected String rhsOperand;
    protected Reward reward;
    
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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public BasicParam getLhsBasicParam() {
        return lhsBasicParam;
    }

    public void setLhsBasicParam(BasicParam lhsBasicParam) {
        this.lhsBasicParam = lhsBasicParam;
    }

    public Operator getLhsOperator() {
        return lhsOperator;
    }

    public void setLhsOperator(Operator lhsOperator) {
        this.lhsOperator = lhsOperator;
    }

    public String getLhsOperand() {
        return lhsOperand;
    }

    public void setLhsOperand(String lhsOperand) {
        this.lhsOperand = lhsOperand;
    }

    public CustomizedPlugin getCustomizedPlugin() {
        return customizedPlugin;
    }

    public void setCustomizedPlugin(CustomizedPlugin customizedPlugin) {
        this.customizedPlugin = customizedPlugin;
    }

    public Operator getRhsPluginOperator() {
        return rhsPluginOperator;
    }

    public void setRhsPluginOperator(Operator rhsPluginOperator) {
        this.rhsPluginOperator = rhsPluginOperator;
    }

    public String getRhsValue() {
        return rhsValue;
    }

    public void setRhsValue(String rhsValue) {
        this.rhsValue = rhsValue;
    }

    public BasicParam getRhsBasicParam() {
        return rhsBasicParam;
    }

    public void setRhsBasicParam(BasicParam rhsBasicParam) {
        this.rhsBasicParam = rhsBasicParam;
    }

    public Operator getRhsOperator() {
        return rhsOperator;
    }

    public void setRhsOperator(Operator rhsOperator) {
        this.rhsOperator = rhsOperator;
    }

    public String getRhsOperand() {
        return rhsOperand;
    }

    public void setRhsOperand(String rhsOperand) {
        this.rhsOperand = rhsOperand;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "Rule{" + "id=" + id + ", ruleTemplate=" + ruleTemplate + ", action=" + action + ", lhsBasicParam=" + lhsBasicParam + ", lhsOperator=" + lhsOperator + ", lhsOperand=" + lhsOperand + ", customizedPlugin=" + customizedPlugin + ", rhsPluginOperator=" + rhsPluginOperator + ", rhsValue=" + rhsValue + ", rhsBasicParam=" + rhsBasicParam + ", rhsOperator=" + rhsOperator + ", rhsOperand=" + rhsOperand + ", reward=" + reward + ", content=" + content + '}';
    }
}