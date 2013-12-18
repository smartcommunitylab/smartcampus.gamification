package eu.trentorise.game.plugin.point.service;

import eu.trentorise.game.plugin.point.model.rule.PointTemplateRule;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.response.SuccessResponse;
import org.springframework.stereotype.Service;


@Service("mockPointRuleManager")
public class MockPointRuleManager extends MockResponder implements IPointRuleManager {

    @Override
    public SuccessResponse add(PointTemplateRule rule) {
        return this.getPositiveResponse();
    }
}