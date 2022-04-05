package eu.trentorise.game.api.rest;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.TeamDTO;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.utils.Converter;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@Profile({"sec", "no-sec"})
public class TeamController {

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private Converter converter;

    @Autowired
    private PlayerController playerController;

    @RequestMapping(method = RequestMethod.POST,
            value = "/data/game/{gameId}/team/{teamId}", consumes = {"application/json"})
    @Operation(summary = "Create team")
    public void createTeam(@PathVariable String gameId,
            @RequestBody TeamDTO team) {
        gameId = decodePathVariable(gameId);
        // check if player already exists
        if (playerSrv.readTeam(gameId, team.getPlayerId()) != null) {
            throw new IllegalArgumentException(
                    String.format("Team %s already exists in game %s", team.getPlayerId(), gameId));
        }

        team.setGameId(gameId);
        TeamState t = converter.convertTeam(team);
        playerSrv.saveTeam(t);
    }

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/data/game/{gameId}/team/{teamId}")
    @Operation(summary = "Delete team")
    public void deleteTeam(@PathVariable String gameId,
            @PathVariable String teamId) {
        gameId = decodePathVariable(gameId);
        teamId = decodePathVariable(teamId);
        playerController.deletePlayer(gameId, teamId);
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/data/game/{gameId}/team/{teamId}/members",
            produces = {"application/json"})
    @Operation(summary = "Get team members")
    public Collection<String> readTeamMembers(
            @PathVariable String gameId, @PathVariable String teamId) {
        gameId = decodePathVariable(gameId);
        teamId = decodePathVariable(teamId);
        TeamState team = playerSrv.readTeam(gameId, teamId);
        return team != null ? team.getMembers() : new ArrayList<String>();
    }

    @RequestMapping(method = RequestMethod.PUT,
            value = "/data/game/{gameId}/team/{teamId}/members",
            consumes = {"application/json"})
    @Operation(summary = "Edit team")
    public void updateTeamMembers(@PathVariable String gameId,
            @PathVariable String teamId, @RequestBody List<String> members) {

        gameId = decodePathVariable(gameId);
        teamId = decodePathVariable(teamId);
        TeamState team = playerSrv.readTeam(gameId, teamId);
        if (team != null) {
            team.setMembers(members);
            playerSrv.saveTeam(team);
        }
    }

    @RequestMapping(method = RequestMethod.PUT,
            value = "/data/game/{gameId}/team/{teamId}/members/{playerId}")
    @Operation(summary = "Add team member")
    public void addTeamMember(@PathVariable String gameId,
            @PathVariable String teamId, @PathVariable String playerId) {
        gameId = decodePathVariable(gameId);
        teamId = decodePathVariable(teamId);
        playerId = decodePathVariable(playerId);
        TeamState team = playerSrv.readTeam(gameId, teamId);
        if (team != null) {
            if (!team.getMembers().contains(playerId)) {
                team.getMembers().add(playerId);
            }
            playerSrv.saveTeam(team);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/data/game/{gameId}/team/{teamId}/members/{playerId}")
    @Operation(summary = "Delete team member")
    public void removeTeamMember(@PathVariable String gameId,
            @PathVariable String teamId, @PathVariable String playerId) {

        gameId = decodePathVariable(gameId);
        teamId = decodePathVariable(teamId);
        playerId = decodePathVariable(playerId);
        TeamState team = playerSrv.readTeam(gameId, teamId);
        if (team != null) {
            team.getMembers().remove(playerId);
            playerSrv.saveTeam(team);
        }
    }
}
