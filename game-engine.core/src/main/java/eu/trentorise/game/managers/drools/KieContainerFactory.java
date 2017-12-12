package eu.trentorise.game.managers.drools;

import org.kie.api.runtime.KieContainer;

public interface KieContainerFactory {

    KieContainer getContainer(String gameId);

    KieContainer purgeContainer(String gameId);
}
