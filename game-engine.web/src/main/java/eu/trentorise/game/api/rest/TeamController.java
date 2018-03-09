package eu.trentorise.game.api.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.TeamDTO;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.utils.Converter;
import io.swagger.annotations.ApiOperation;

@RestController
public class TeamController {

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private Converter converter;

    @Autowired
    private PlayerController playerController;

    // Create a team
    // POST /data/game/{id}/team/{teamId}
    //
    // ­ Error if the team ID already exists
    // ­ In body specify name (optional), member list (optional)
    //
    @RequestMapping(method = RequestMethod.POST,
            value = "/data/game/{domain}/{gameId}/team/{teamId}", consumes = {"application/json"},
            produces = {"application/json"})
    @ApiOperation(value = "Create team")
    public void createTeam(@PathVariable String domain, @PathVariable String gameId,
            @RequestBody TeamDTO team) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        // check if player already exists
        if (playerSrv.readTeam(gameId, team.getPlayerId()) != null) {
            throw new IllegalArgumentException(
                    String.format("Team %s already exists in game %s", team.getPlayerId(), gameId));
        }

        team.setGameId(gameId);
        TeamState t = converter.convertTeam(team);
        playerSrv.saveTeam(t);
    }

    // Delete a team
    // DELETE /data/game/{id}/team/{teamId}

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/data/game/{domain}/{gameId}/team/{teamId}", consumes = {"application/json"},
            produces = {"application/json"})
    @ApiOperation(value = "Delte team")
    public void deleteTeam(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String teamId) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            teamId = URLDecoder.decode(teamId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }
        playerController.deletePlayer(domain, gameId, teamId);
    }

    // Read team members
    // GET /data/game/{id}/team/{teamId}/members

    @RequestMapping(method = RequestMethod.GET,
            value = "/data/game/{domain}/{gameId}/team/{teamId}/members",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Get team members")
    public Collection<String> readTeamMembers(@PathVariable String domain,
            @PathVariable String gameId, @PathVariable String teamId) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            teamId = URLDecoder.decode(teamId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }

        TeamState team = playerSrv.readTeam(gameId, teamId);
        return team != null ? team.getMembers() : new ArrayList<String>();
    }

    // Update team members
    // PUT /data/game/{id}/team/{teamId}/members

    @RequestMapping(method = RequestMethod.PUT,
            value = "/data/game/{domain}/{gameId}/team/{teamId}/members",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Edit team")
    public void updateTeamMembers(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String teamId, @RequestBody List<String> members) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            teamId = URLDecoder.decode(teamId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }
        TeamState team = playerSrv.readTeam(gameId, teamId);
        if (team != null) {
            team.setMembers(members);
            playerSrv.saveTeam(team);
        }
    }

    // Add team member
    // PUT /data/game/{id}/team/{teamId}/members/{playerId}

    @RequestMapping(method = RequestMethod.PUT,
            value = "/data/game/{domain}/{gameId}/team/{teamId}/members/{playerId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Add team member")
    public void addTeamMember(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String teamId, @PathVariable String playerId,
            @RequestBody List<String> members) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            teamId = URLDecoder.decode(teamId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }
        TeamState team = playerSrv.readTeam(gameId, teamId);
        if (team != null) {
            if (!team.getMembers().contains(playerId)) {
                team.getMembers().add(playerId);
            }
            playerSrv.saveTeam(team);
        }
    }

    // Remove a team member
    // DELETE /data/game/{id}/team/{teamId}/members/{playerId}

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/data/game/{domain}/{gameId}/team/{teamId}/members/{playerId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Delete team member")
    public void removeTeamMember(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String teamId, @PathVariable String playerId,
            @RequestBody List<String> members) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            teamId = URLDecoder.decode(teamId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }
        TeamState team = playerSrv.readTeam(gameId, teamId);
        if (team != null) {
            team.getMembers().remove(playerId);
            playerSrv.saveTeam(team);
        }
    }
}
