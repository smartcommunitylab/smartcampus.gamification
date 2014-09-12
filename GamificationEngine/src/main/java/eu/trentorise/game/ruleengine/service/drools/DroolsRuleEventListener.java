package eu.trentorise.game.ruleengine.service.drools;

import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DroolsRuleEventListener extends org.kie.api.event.rule.DefaultAgendaEventListener {
    
    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent event) {
        final Rule rule = event.getMatch().getRule();
	final Logger log = LoggerFactory.getLogger(rule.getPackageName() + "." + rule.getName());
	log.debug(event.getClass().getSimpleName());
    }
}