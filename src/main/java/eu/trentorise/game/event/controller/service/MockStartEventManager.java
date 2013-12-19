package eu.trentorise.game.event.controller.service;

import eu.trentorise.game.event.model.StartEvent;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.response.SuccessResponse;
import org.springframework.stereotype.Service;


@Service("mockStartEventManager")
public class MockStartEventManager extends MockResponder implements IStartEventManager {

    @Override
    public SuccessResponse runEvent(StartEvent event) {
        return this.getPositiveResponse();
    }
}