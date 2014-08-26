package eu.trentorise.game.plugin.badgecollection.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.badgecollection.response.BadgeCollectionResponse;
import eu.trentorise.game.plugin.badgecollection.response.BadgeResponse;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.utils.rest.RestExceptionHandler;
import eu.trentorise.utils.rest.crud.AbstractCrudRestController;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.RestCrudHelper;
import eu.trentorise.utils.rest.crud.RestCrudResponseHelper;
import eu.trentorise.utils.rest.crud.RestCrudResultHelper;
import eu.trentorise.utils.web.WebFile;
import java.util.Collection;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Luca Piras
 */
@Controller("badgeCollectionPluginBadgeController")
public class BadgeController extends AbstractCrudRestController<Badge, BadgeCollectionPlugin, Badge> {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public BadgeController() {
        super(IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH,
              LoggerFactory.getLogger(BadgeController.class.getName()));
    }
    
    
    //CREATE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_PATH, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createBadge(@PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM) Integer cusPlugId,
                                            @RequestBody Badge element,
                                            UriComponentsBuilder builder) {
        
        //TODO: validate the plugin id, it has to be the same as the id of the
        //main plugin related to this customizedPlugin
        //TODO: this plugin knows what is the id of the main related plugin,
        //think how to implement it
        element = this.decodeBadgeFile(element);
        element.setBadgeCollection(this.makeBadgeCollectionPlugin(cusPlugId));
        
        return super.createResource(element, builder);
    }
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_PATH, method = RequestMethod.GET)
    public @ResponseBody BadgeCollectionResponse readBadges(
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM) Integer cusPlugId) {
        
        Collection<Badge> results = super.readResources(this.makeBadgeCollectionPlugin(cusPlugId));
        
        results = this.encodeBadgeCollectionFile(results);
        
        BadgeCollectionResponse response = new BadgeCollectionResponse();
        response.setBadges(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody BadgeResponse readBadgeById(
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH_PARAM) Integer badgeId) {
        
        BadgeCollectionPlugin badgeCollectionPlugin = this.makeBadgeCollectionPlugin(cusPlugId);
        
        Badge element = new Badge();
        element.setId(badgeId);
        element.setBadgeCollection(badgeCollectionPlugin);
        
        Badge result = super.readResourceById(element);
        
        result = this.encodeBadgeFile(result);
        
        BadgeResponse response = new BadgeResponse();
        response.setBadge(result);
        
        return response;
    }
    
    
    //UPDATE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH, method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Void> updateBadge(
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH_PARAM) Integer badgeId,
                         @RequestBody Badge element,
                         UriComponentsBuilder builder) {
        
        //TODO: validate the plugin id, it has to be the same as the id of the
        //main plugin related to this customizedPlugin
        
        BadgeCollectionPlugin badgeCollectionPlugin = this.makeBadgeCollectionPlugin(cusPlugId);
        
        element.setId(badgeId);
        element.setBadgeCollection(badgeCollectionPlugin);
        
        return super.updateResource(element, builder);
    }
    
    
    //DELETE
    @RequestMapping(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH, method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Void> deleteBadge(
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM) Integer cusPlugId,
                         @PathVariable(value = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH_PARAM) Integer badgeId) {
        //TODO: validate the plugin id, it has to be the same as the id of the
        //main plugin related to this customizedPlugin
        
        BadgeCollectionPlugin badgeCollectionPlugin = this.makeBadgeCollectionPlugin(cusPlugId);
        
        Badge element = new Badge();
        element.setId(badgeId);
        element.setBadgeCollection(badgeCollectionPlugin);
        
        return super.deleteResource(element);
    }
    

    @Override
    protected Map<String, Object> populateUriVariables(Badge containerWithIds, Badge result, Map<String, Object> uriVariables) {
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM, result.getBadgeCollection().getId());
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
    }
    
    
    protected Badge decodeBadgeFile(Badge element) {
        //TODO: implement this part: the file that arrives is encoded with
        //base64 and it has to be decoded in order to save it into the db
        /*ObjectMapper mapper = new ObjectMapper();
        
        WebFile image = element.getImage();
        
        String encodedContent = image.getFileContent();
        byte[] encoded = mapper.convertValue(encodedContent, byte[].class);
        //byte[] decoded = Base64.decode(encoded);
        String decoded = mapper.convertValue(encoded, String.class);
        
        image.setFileContent(decoded);*/
        
        return element;
    }
    
    protected Badge encodeBadgeFile(Badge element) {
        //TODO: implement this part
        //ObjectMapper mapper = new ObjectMapper();
        
        WebFile image = element.getImage();
        image.setFileContent("Ly9UT0RPOiBpbXBsZW1lbnQgdGhpcyBwYXJ0");
        
        /*String decodedContent = image.getFileContent();
        byte[] encoded = mapper.convertValue(encodedContent, byte[].class);
        //byte[] decoded = Base64.decode(encoded);
        String decoded = mapper.convertValue(encoded, String.class);
        
        image.setFileContent(decoded);*/
        
        return element;
    }
    
    protected Collection<Badge> encodeBadgeCollectionFile(Collection<Badge> elements) {
        for (Badge badge : elements) {
            this.encodeBadgeFile(badge);
        }
        
        return elements;
    }
    
    protected BadgeCollectionPlugin makeBadgeCollectionPlugin(Integer cusPlugId) {
        Plugin plugin = this.makePlugin();
        
        BadgeCollectionPlugin badgeCollectionPlugin = new BadgeCollectionPlugin();
        badgeCollectionPlugin.setId(cusPlugId);
        badgeCollectionPlugin.setPlugin(plugin);
        
        return badgeCollectionPlugin;
    }
    
    protected Plugin makePlugin() {
        Plugin plugin = new Plugin();
        plugin.setId(IGameConstants.SEQUENCE_INITIAL_VALUE + 1);
        return plugin;
    }

    
    @Qualifier("mockBadgeCollectionPluginBadgeManager")
    @Autowired
    public void setManager(IRestCrudManager<Badge, BadgeCollectionPlugin, Badge> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<Badge, BadgeCollectionPlugin, Badge> restCrudHelper) {
        this.restCrudHelper = restCrudHelper;
    }

    @Qualifier("restCrudResultHelper")
    @Autowired
    public void setRestCrudResultHelper(RestCrudResultHelper restCrudResultHelper) {
        this.restCrudResultHelper = restCrudResultHelper;
    }
    
    @Qualifier("restCrudResponseHelper")
    @Autowired
    public void setRestCrudResponseHelper(RestCrudResponseHelper restCrudResponseHelper) {
        this.restCrudResponseHelper = restCrudResponseHelper;
    }
    
    @Qualifier("restExceptionHandler")
    @Autowired
    public void setRestExceptionHandler(RestExceptionHandler restExceptionHandler) {
        this.restExceptionHandler = restExceptionHandler;
    }
}