package eu.trentorise.game.api.rest;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import eu.trentorise.game.managers.NotificationManager;
import eu.trentorise.game.model.core.Notification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@RestController
@Profile({"sec", "no-sec"})
public class NotificationController {

    @Autowired
    private NotificationManager notificationSrv;

    @RequestMapping(method = RequestMethod.GET,
            value = "/notification/game/{gameId}/player/{playerId}",
            produces = {"application/json"})
    @Operation(summary = "Get player notifications")
    @Parameters({
            @Parameter(name = "page", description = "Results page you want to retrieve "),
            @Parameter(name = "size", description = "Number of records per page."),})
    public List<Notification> readPlayerNotification(
            @PathVariable String gameId, @PathVariable String playerId,
            @Parameter(hidden = true) Pageable pageable, @RequestParam(defaultValue = "-1") long fromTs,
            @RequestParam(defaultValue = "-1") long toTs,
            @RequestParam(required = false) List<String> includeTypes,
            @RequestParam(required = false) List<String> excludeTypes) {

        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);
        return notificationSrv.readNotifications(gameId, playerId, fromTs, toTs, includeTypes,
                excludeTypes, pageable);

    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/notification/game/{gameId}/player/{playerId}/grouped",
            produces = {"application/json"})
    @Operation(summary = "Get player notifications")
    @Parameters({
            @Parameter(name = "page", description = "Results page you want to retrieve "),
            @Parameter(name = "size", description = "Number of records per page."),})
    public Map<String, Collection<Notification>> readPlayerNotificationGrouped(
            @PathVariable String gameId, @PathVariable String playerId,
            @Parameter(hidden = true) Pageable pageable, @RequestParam(defaultValue = "-1") long fromTs,
            @RequestParam(defaultValue = "-1") long toTs,
            @RequestParam(required = false) List<String> includeTypes,
            @RequestParam(required = false) List<String> excludeTypes) {

        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);
        List<Notification> notifications = notificationSrv.readNotifications(gameId, playerId, fromTs, toTs, includeTypes, excludeTypes, pageable);
        
        Multimap<String, Notification> notificationsMap = ArrayListMultimap.create();
        
        notifications.forEach(x -> {
        	notificationsMap.put(x.getClass().getSimpleName(), x);
        });
        
        return notificationsMap.asMap();
    }    
    
    
    @RequestMapping(method = RequestMethod.GET,
            value = "/notification/game/{gameId}/team/{teamId}",
            produces = {"application/json"})
    @Operation(summary = "Get team notifications")
    @Parameters({
            @Parameter(name = "page", description = "Results page you want to retrieve "),
            @Parameter(name = "size", description = "Number of records per page."),})
    public List<Notification> readTeamNotification(
            @PathVariable String gameId, @PathVariable String teamId, @Parameter(hidden = true) Pageable pageable,
            @RequestParam(defaultValue = "-1") long fromTs,
            @RequestParam(defaultValue = "-1") long toTs,
            @RequestParam(required = false) List<String> includeTypes,
            @RequestParam(required = false) List<String> excludeTypes) {
        return readPlayerNotification(gameId, teamId, pageable, fromTs, toTs, includeTypes,
                excludeTypes);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/notification/game/{gameId}",
            produces = {"application/json"})
    @Operation(summary = "Get game notifications")
    @Parameters({
            @Parameter(name = "page", description = "Results page you want to retrieve "),
            @Parameter(name = "size", description = "Number of records per page.")
            })
    public List<Notification> readNotification(
            @PathVariable String gameId, @Parameter(hidden = true) Pageable pageable,
            @RequestParam(defaultValue = "-1") long fromTs,
            @RequestParam(defaultValue = "-1") long toTs,
            @RequestParam(required = false) List<String> includeTypes,
            @RequestParam(required = false) List<String> excludeTypes) {
        gameId = decodePathVariable(gameId);

        return notificationSrv.readNotifications(gameId, fromTs, toTs, includeTypes, excludeTypes,
                pageable);
    }

    // TODO: consider a possibility to write notifications
}
