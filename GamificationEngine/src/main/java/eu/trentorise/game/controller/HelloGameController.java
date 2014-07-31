package eu.trentorise.game.controller;

import eu.trentorise.game.request.HelloGameRequest;
import eu.trentorise.game.model.backpack.Badge;
import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import eu.trentorise.game.ruleengine.service.IRuleEngineManager;
import javax.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
       
/**
 *
 * @author Luca Piras
 */
@Controller("helloGameController")
@SessionAttributes(value = HelloGameController.CO)
@RequestMapping(value = HelloGameController.VIEW_ABSOLUTE)
public class HelloGameController extends AbstractController<HelloGameRequest> {
    
    public static final String VIEW_WITHOUT_PATH = "hellogame";
    public static final String VIEW = IGameConstants.VIEW_PATH + VIEW_WITHOUT_PATH;
    public static final String VIEW_ABSOLUTE = VIEW + IGameConstants.VIEW_PAGE_EXTENSION_SEPARATOR + IGameConstants.VIEW_PAGE_EXTENSION;    
    
    public static final String VIEW_INTERNAL =  IGameConstants.VIEW_INTERNAL_NAMESPACE + VIEW_WITHOUT_PATH;
    
    public static final String CO = "helloGameCO";
    
    public HelloGameController() {
        super(VIEW_ABSOLUTE, VIEW_INTERNAL, CO,
              LoggerFactory.getLogger(HelloGameController.class.getName()));
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model, HttpSession session) throws Exception {
        
        super.manageGet(model, session);
        
        //CDBFFFO deactivated to disable connection with the db
        /*boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        
        //TODO: manage the value of the gamification approach id
        rulesEngineManager.runEngine(null, 
                                     GamificationPluginIdentifier.BADGE_PLUGIN);
        
        actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();*/
        
        return this.viewInternal;
    }
    
    @Override
    protected HelloGameRequest initializeNotExistentCommandObject() {
        Badge badge = new Badge("Welcome Badge", new Integer(10000));
        
        badge.setTitle("Welcome to the Gamification Engine!");
        
        HelloGameRequest co = new HelloGameRequest();
        co.setBadge(badge);
        
        return co;
    }
    
    @Override
    protected void initializeExistentCommandObject(HelloGameRequest co) {}
    
    
    @Qualifier("badgeRulesEngineManager")
    @Autowired
    protected IRuleEngineManager rulesEngineManager;
    
    
    //@Autowired
    //protected IGameManager gameManager;
}