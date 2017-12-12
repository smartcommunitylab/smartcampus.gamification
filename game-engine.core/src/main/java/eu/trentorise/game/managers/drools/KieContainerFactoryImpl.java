package eu.trentorise.game.managers.drools;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.managers.DroolsEngine;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.model.core.FSRule;
import eu.trentorise.game.model.core.Rule;
import eu.trentorise.game.services.GameService;

@Component
public class KieContainerFactoryImpl implements KieContainerFactory {

    private Map<String, KieContainer> containersCache = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(DroolsEngine.class);

    private KieServices kieServices = KieServices.Factory.get();

    @Autowired
    private GameService gameSrv;

    private void checkGameId(String gameId) {
        if (StringUtils.isBlank(gameId)) {
            throw new IllegalArgumentException("gameId cannot be blank");
        }
    }


    private KieContainer containerInstance(String gameId) {
        checkGameId(gameId);
        loadGameRules(gameId);
        KieContainer kieContainer =
                kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        return kieContainer;
    }

    @Override
    public KieContainer getContainer(String gameId) {

        checkGameId(gameId);

        KieContainer containerCached = containersCache.get(gameId);
        if (containerCached != null) {
            LogHub.info(gameId, logger, "found a cached container for game {}", gameId);
            return containerCached;
        } else {
            LogHub.info(gameId, logger, "no container found for game {}", gameId);
            KieContainer newContainer = containerInstance(gameId);
            LogHub.info(gameId, logger, "created new container for game {}", gameId);
            containersCache.put(gameId, newContainer);
            LogHub.info(gameId, logger, "cached container for game {}", gameId);
            return newContainer;

        }
    }

    @Override
    public KieContainer purgeContainer(String gameId) {
        checkGameId(gameId);

        KieContainer purged = containersCache.remove(gameId);;
        if (purged != null) {
            LogHub.info(gameId, logger, "purged container for game {}", gameId);
        } else {
            LogHub.info(gameId, logger, "trying to purge not existing container for game {}",
                    gameId);
        }
        return purged;
    }

    private void loadGameRules(String gameId) {
        checkGameId(gameId);

        KieFileSystem kfs = kieServices.newKieFileSystem();
        RuleLoader ruleLoader = new RuleLoader(gameId);
        // load core.drl

        Resource coreRes;
        try {
            coreRes = ruleLoader.load("classpath://rules/core.drl");
            kfs.write(coreRes);
            LogHub.info(gameId, logger, "Core rules loaded");
        } catch (MalformedURLException e) {
            LogHub.info(gameId, logger, "Exception loading core.drl");
        }

        // load rules

        Game game = gameSrv.loadGameDefinitionById(gameId);

        if (game != null && game.getRules() != null) {
            for (String rule : game.getRules()) {
                Resource r1;
                try {
                    r1 = ruleLoader.load(rule);
                    // fix to not load constant file
                    if (r1 != null) {
                        kfs.write(r1);
                        LogHub.debug(gameId, logger, "{} loaded", rule);
                    }
                } catch (MalformedURLException e) {
                    LogHub.error(gameId, logger, "Malformed URL loading rule {}, rule not loaded",
                            rule);
                } catch (RuntimeException e) {
                    LogHub.error(gameId, logger, "Exception loading rule {}", rule);
                }
            }
        }
        kieServices.newKieBuilder(kfs).buildAll();
        LogHub.info(gameId, logger, "Rules repository built");
    }

    private class RuleLoader {
        private String gameId;

        public RuleLoader(String gameId) {
            this.gameId = gameId;
        }

        public boolean isConstantsRule(String ruleUrl) {
            boolean classpathCheck = ruleUrl.startsWith(ClasspathRule.URL_PROTOCOL)
                    && ruleUrl.contains("/constants");
            boolean fsCheck =
                    ruleUrl.startsWith(FSRule.URL_PROTOCOL) && ruleUrl.contains("/constants");
            boolean dbCheck = ruleUrl.startsWith(DBRule.URL_PROTOCOL);
            if (dbCheck) {
                Rule r = gameSrv.loadRule(gameId, ruleUrl);
                dbCheck = r != null && r.getName() != null && r.getName().equals("constants");
            }

            return classpathCheck || fsCheck || dbCheck;
        }

        public Resource load(String ruleUrl) throws MalformedURLException {
            Resource res = null;
            String url = null;
            if (isConstantsRule(ruleUrl)) {
                return null;
            }
            if (ruleUrl.startsWith(ClasspathRule.URL_PROTOCOL)) {
                url = ruleUrl.substring(ClasspathRule.URL_PROTOCOL.length());
                res = kieServices.getResources().newClassPathResource(url);
            } else if (ruleUrl.startsWith(FSRule.URL_PROTOCOL)) {
                url = ruleUrl.substring(FSRule.URL_PROTOCOL.length());
                res = kieServices.getResources().newFileSystemResource(url);
            } else if (ruleUrl.startsWith(DBRule.URL_PROTOCOL)) {
                Rule r = gameSrv.loadRule(gameId, ruleUrl);
                if (r != null) {
                    res = kieServices.getResources()
                            .newReaderResource(new StringReader(((DBRule) r).getContent()));
                    res.setSourcePath(
                            "rules/" + r.getGameId() + "/" + ((DBRule) r).getId() + ".drl");
                } else {
                    LogHub.error(ruleUrl, logger, "DBRule {} not exist", url);
                }
            } else {
                throw new MalformedURLException("resource URL not supported");
            }
            return res;
        }
    }


}
