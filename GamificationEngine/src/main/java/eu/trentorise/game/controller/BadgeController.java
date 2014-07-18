package eu.trentorise.game.controller;

import eu.trentorise.game.model.backpack.Badge;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * @author Luca Piras
 */
@Controller("DEPRECATEDbadgeController")
@RequestMapping(IGameConstants.SERVICE_PATH)
@Deprecated
public class BadgeController {
    
    //TODO: rethink about this service
    protected static final String template = "Badge's name: %s";

    @RequestMapping(method = RequestMethod.GET, 
                    value = "/badge" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody Badge getBadge(@RequestParam(value = "title", 
                                                      required = false, 
                                                      defaultValue = "Basic Mayor") String title) {
        
        //TODO: rethink about this service
        Badge badge = new Badge(String.format(template, title), 1000);
        
        return badge;
    }
}