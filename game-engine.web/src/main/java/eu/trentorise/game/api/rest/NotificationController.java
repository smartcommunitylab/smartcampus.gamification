package eu.trentorise.game.api.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.managers.NotificationManager;
import eu.trentorise.game.model.core.Notification;
import io.swagger.annotations.ApiOperation;

@RestController
public class NotificationController {

	@Autowired
	private NotificationManager notificationSrv;

	// Read user notifications
	// GET /notification/game/{id}/player/{playerId}?start=<num, optional,
	// default=0>&count=<num,optional, default=­1 to retrieve all>

	@RequestMapping(method = RequestMethod.GET, value = "/notification/game/{gameId}/player/{playerId}", produces = {
			"application/json" })
	@ApiOperation(value = "Get player notifications")
	public List<Notification> readPlayerNotification(@PathVariable String gameId, @PathVariable String playerId,
			@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "-1") int count,
			@RequestParam(required = false) Long timestamp) {

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
		if (timestamp != null) {
			return notificationSrv.readNotifications(gameId, playerId, timestamp);
		} else {
			return notificationSrv.readNotifications(gameId, playerId);
		}
	}

	// Read team notifications
	// GET /notification/game/{id}/team/{teamId}?start=<num, optional,
	// default=0>&count=<num,optional, default=­1 to retrieve all>

	@RequestMapping(method = RequestMethod.GET, value = "/notification/game/{gameId}/team/{teamId}", produces = {
			"application/json" })
	@ApiOperation(value = "Get team notifications")
	public List<Notification> readTeamNotification(@PathVariable String gameId, @PathVariable String teamId,
			@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "-1") int count,
			@RequestParam(required = false) Long timestamp) {

		return readPlayerNotification(gameId, teamId, start, count, timestamp);
	}

	// Read game notifications
	// GET /notification/game/{id}?start=<num, optional,
	// default=0>&count=<num,optional,
	// default=­1 to retrieve all>

	@RequestMapping(method = RequestMethod.GET, value = "/notification/game/{gameId}", produces = {
			"application/json" })
	@ApiOperation(value = "Get game notifications")
	public List<Notification> readNotification(@PathVariable String gameId, @RequestParam(defaultValue = "0") int start,
			@RequestParam(defaultValue = "-1") int count, @RequestParam(required = false) Long timestamp) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		if (timestamp != null) {
			return notificationSrv.readNotifications(gameId, timestamp);
		} else {
			return notificationSrv.readNotifications(gameId);
		}
	}

	// TODO: consider a possibility to write notifications
}
