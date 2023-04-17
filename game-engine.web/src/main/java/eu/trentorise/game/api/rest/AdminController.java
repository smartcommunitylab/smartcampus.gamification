package eu.trentorise.game.api.rest;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.utils.Converter;
import eu.trentorise.game.utils.JsonDB;

@RestController
@RequestMapping(value = "/admin")
@Profile({"sec", "no-sec"})
public class AdminController {

    @Autowired
    private PlayerService playerSrv;
    
    @Autowired
    private Converter converter;
    
	@Autowired
	private JsonDB jsonDB;

    @GetMapping(value="/data/game/{gameId}/player")
    public Page<PlayerStateDTO> readPlayerStates(@PathVariable String gameId,
            Pageable pageable, @RequestParam(required = false) String playerFilter) {
        gameId = decodePathVariable(gameId);
        List<PlayerStateDTO> resList = new ArrayList<PlayerStateDTO>();
        Page<PlayerState> page = null;
        final boolean hideHiddenChallenges = false;
        final boolean mergeGroupChallenges = true;
        if (playerFilter == null) {
            page = playerSrv.loadStates(gameId, pageable, mergeGroupChallenges,
                    hideHiddenChallenges);
        } else {
            page = playerSrv.loadStates(gameId, playerFilter, pageable, mergeGroupChallenges,
                    hideHiddenChallenges);
        }
        for (PlayerState ps : page) {
            resList.add(converter.convertPlayerState(ps));
        }

        PageImpl<PlayerStateDTO> res =
                new PageImpl<PlayerStateDTO>(resList, pageable, page.getTotalElements());

        return res;
    }

    @GetMapping(value = "/data/game/{gameId}/player/{playerId}")
    public PlayerStateDTO readPlayerState(@PathVariable String gameId,
            @PathVariable String playerId, Pageable pageable,
            @RequestParam(required = false) String playerFilter) {
        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);
        final boolean mergeChallenges = true;
        final boolean hideHiddenChallenges = false;
        return converter
                .convertPlayerState(playerSrv.loadState(gameId, playerId, true,
                        mergeChallenges, mergeChallenges, hideHiddenChallenges));
    }
    
    @GetMapping("/exportJsonDB")
	public void exportJsonDB() throws Exception {
		jsonDB.exportDB();
	}
    
    @GetMapping("/importJsonDB")
	public void importJsonDB() throws Exception {
		jsonDB.importDB();
	}
}
