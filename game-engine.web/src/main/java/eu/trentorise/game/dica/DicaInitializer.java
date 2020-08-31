package eu.trentorise.game.dica;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.model.AuthUser;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.sec.UsersProvider;
import eu.trentorise.game.service.DefaultIdentityLookup;
import eu.trentorise.game.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class DicaInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DicaInitializer.class);

    @Autowired
    private UsersProvider usersProvider;

    @Autowired
    private DicaGameFactory dicaGameFactory;

    @Autowired
    private GameService gameSrv;

    @Autowired
    private Environment env;


    @Value("${game.createDica}")
    private boolean createDicaGame;

    @PostConstruct
    private void initDicas() {
        if (createDicaGame) {
            LogHub.info(null, logger, "createDica configuration active...create dica games");
            Game g = null;
            boolean secProfileActive = Arrays.binarySearch(env.getActiveProfiles(), "sec") >= 0;
            if (secProfileActive) {
                LogHub.info(null, logger, "sec profile active..create sample game for every user");
                for (AuthUser user : usersProvider.getUsers()) {
                    g = dicaGameFactory.createGame(null, null, null, user.getUsername());
                    if (g != null) {
                        gameSrv.startupTasks(g.getId());
                    }
                }
            } else {
                LogHub.info(null, logger,
                        "no-sec profile active..create sample game for default user");
                // initialize dica-game for default user in no-sec env
                g = dicaGameFactory.createGame(null, null, null, DefaultIdentityLookup.DEFAULT_USER);
                if (g != null) {
                    gameSrv.startupTasks(g.getId());
                }
            }
        } else {
            LogHub.info(null, logger, "createDica configuration inactive");
        }
    }
}
