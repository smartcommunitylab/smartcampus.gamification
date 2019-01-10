package eu.trentorise.game.core.config;

import java.io.StringReader;
import java.net.MalformedURLException;

import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import eu.trentorise.game.managers.GameManager;
import eu.trentorise.game.managers.GameWorkflow;
import eu.trentorise.game.managers.drools.KieContainerFactory;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.model.core.FSRule;
import eu.trentorise.game.model.core.Rule;
import eu.trentorise.game.services.Workflow;

@Configuration
public class TestCoreConfiguration {

    /**
     * Use simple gameWorkflow for test purpose, without execution queue and ExecutorService
     * 
     * @return GameWorkflow
     */
    @Bean
    @Primary
    public Workflow workflow() {
        return new GameWorkflow();
    }


    /**
     * Return KieContainerFactory without use the rule cache mechanism
     * 
     * TODO: must be improve..to much code duplication with the main version
     * 
     * @return KieContainerFactory
     */

    @Bean
    @Primary
    public KieContainerFactory kieContainerFactory() {
        return new KieContainerFactory() {

            private KieServices kieServices = KieServices.Factory.get();

            @Autowired
            private GameManager gameSrv;

            @Override
            public KieContainer purgeContainer(String gameId) {
                return null;
            }

            // don't cache anything
            @Override
            public KieContainer getContainer(String gameId) {

                KieFileSystem kfs = kieServices.newKieFileSystem();
                RuleLoader ruleLoader = new RuleLoader(gameId);
                // load core.drl

                Resource coreRes;
                try {
                    coreRes = ruleLoader.load("classpath://rules/core.drl");
                    kfs.write(coreRes);
                    // load rules for group challenges rewards
                    Resource groupChallengesRewardRules =
                            ruleLoader.load("classpath://rules/groupChallengeReward.drl");
                    kfs.write(groupChallengesRewardRules);
                } catch (MalformedURLException e) {
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
                            }
                        } catch (MalformedURLException e) {
                        } catch (RuntimeException e) {
                        }
                    }
                }
                kieServices.newKieBuilder(kfs).buildAll();
                return kieServices
                        .newKieContainer(kieServices.getRepository().getDefaultReleaseId());
            }

            class RuleLoader {
                private String gameId;

                public RuleLoader(String gameId) {
                    this.gameId = gameId;
                }

                public boolean isConstantsRule(String ruleUrl) {
                    boolean classpathCheck = ruleUrl.startsWith(ClasspathRule.URL_PROTOCOL)
                            && ruleUrl.contains("/constants");
                    boolean fsCheck = ruleUrl.startsWith(FSRule.URL_PROTOCOL)
                            && ruleUrl.contains("/constants");
                    boolean dbCheck = ruleUrl.startsWith(DBRule.URL_PROTOCOL);
                    if (dbCheck) {
                        Rule r = gameSrv.loadRule(gameId, ruleUrl);
                        dbCheck =
                                r != null && r.getName() != null && r.getName().equals("constants");
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
                        }
                    } else {
                        throw new MalformedURLException("resource URL not supported");
                    }
                    return res;
                }
            }
        };
    }
}