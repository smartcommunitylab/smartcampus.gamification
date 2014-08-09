package eu.trentorise.game.aaa.controller;

import eu.trentorise.game.aaa.container.AuthenticationContainer;
import eu.trentorise.game.aaa.container.IAuthenticationContainer;
import eu.trentorise.game.aaa.request.GameAuthenticationRequest;
import eu.trentorise.game.aaa.service.IGameAuthenticationManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.utils.rest.RestExceptionHandler;
import eu.trentorise.utils.rest.RestHelper;
import eu.trentorise.utils.rest.RestResponseHelper;
import eu.trentorise.utils.rest.RestResultHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * @author Luca Piras
 */
@Controller("gameAuthenticationController")
@RequestMapping(IGameConstants.SERVICE_GAME_AAA_PATH)
public class GameAuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(GameAuthenticationController.class.getName());
    
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public @ResponseBody ResponseEntity<Void> authenticate(@RequestBody GameAuthenticationRequest request) throws Exception {
        ResponseEntity<Void> response = null;
        try {
            IAuthenticationContainer container = new AuthenticationContainer();
            container.setUser(request.getUser());
        
            restHelper.execute(container, manager, "authenticate", 
                               IAuthenticationContainer.class, restResultHelper, logger);
        
            response = restResponseHelper.makeNoContentResponse();
        } catch (Exception ex) {
            restExceptionHandler.handleException(ex, logger);
        }
        
        return response;
    }
    
    
    @Qualifier("mockGameAuthenticationManager")
    @Autowired
    protected IGameAuthenticationManager manager;
    
    
    @Qualifier("restHelper")
    @Autowired
    protected RestHelper restHelper;
    
    @Qualifier("restResultHelper")
    @Autowired
    protected RestResultHelper restResultHelper;
    
    @Qualifier("restResponseHelper")
    @Autowired
    protected RestResponseHelper restResponseHelper;
    
    @Qualifier("restExceptionHandler")
    @Autowired
    protected RestExceptionHandler restExceptionHandler;
}