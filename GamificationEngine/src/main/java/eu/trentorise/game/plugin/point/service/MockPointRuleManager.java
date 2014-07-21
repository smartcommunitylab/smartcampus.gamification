package eu.trentorise.game.plugin.point.service;

import eu.trentorise.game.plugin.point.model.rule.PointRuleTemplate;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.response.GameResponse;
import org.springframework.stereotype.Service;


@Service("mockPointRuleManager")
public class MockPointRuleManager extends MockResponder implements IPointRuleManager {

    @Override
    public GameResponse add(PointRuleTemplate rule) {
        return this.getPositiveResponse();
    }
}