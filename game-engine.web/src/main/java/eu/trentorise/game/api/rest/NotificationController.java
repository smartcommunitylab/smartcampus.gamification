package eu.trentorise.game.api.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.managers.NotificationManager;
import eu.trentorise.game.model.core.Notification;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class NotificationController {

    @Autowired
    private NotificationManager notificationSrv;

    @RequestMapping(method = RequestMethod.GET,
            value = "/notification/game/{domain}/{gameId}/player/{playerId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get player notifications")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve "),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),})
    public List<Notification> readPlayerNotification(@PathVariable String domain,
            @PathVariable String gameId, @PathVariable String playerId,
            @ApiIgnore Pageable pageable, @RequestParam(defaultValue = "-1") long fromTs,
            @RequestParam(defaultValue = "-1") long toTs,
            @RequestParam(required = false) List<String> includeTypes,
            @RequestParam(required = false) List<String> excludeTypes) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }
        return notificationSrv.readNotifications(gameId, playerId, fromTs, toTs, includeTypes,
                excludeTypes, pageable);

    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/notification/game/{domain}/{gameId}/team/{teamId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get team notifications")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve "),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),})
    public List<Notification> readTeamNotification(@PathVariable String domain,
            @PathVariable String gameId, @PathVariable String teamId, @ApiIgnore Pageable pageable,
            @RequestParam(defaultValue = "-1") long fromTs,
            @RequestParam(defaultValue = "-1") long toTs,
            @RequestParam(required = false) List<String> includeTypes,
            @RequestParam(required = false) List<String> excludeTypes) {
        return readPlayerNotification(domain, gameId, teamId, pageable, fromTs, toTs, includeTypes,
                excludeTypes);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/notification/game/{domain}/{gameId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get game notifications")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve "),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),})
    public List<Notification> readNotification(@PathVariable String domain,
            @PathVariable String gameId, @ApiIgnore Pageable pageable,
            @RequestParam(defaultValue = "-1") long fromTs,
            @RequestParam(defaultValue = "-1") long toTs,
            @RequestParam(required = false) List<String> includeTypes,
            @RequestParam(required = false) List<String> excludeTypes) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        return notificationSrv.readNotifications(gameId, fromTs, toTs, includeTypes, excludeTypes,
                pageable);
    }

    // TODO: consider a possibility to write notifications
}
