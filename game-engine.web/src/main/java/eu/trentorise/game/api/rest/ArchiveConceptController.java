package eu.trentorise.game.api.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.managers.ArchiveManager;
import eu.trentorise.game.model.core.ArchivedConcept;
import io.swagger.annotations.ApiOperation;

@RestController
@Profile({ "sec", "no-sec" })
public class ArchiveConceptController {

	@Autowired
    private ArchiveManager archiveSrv;

	@RequestMapping(method = RequestMethod.GET, value = "/data/game/{gameId}/archive", produces = {
			"application/json" })
	@ApiOperation(value = "Read archive concepts for a game with optional filter parameters")
	public List<ArchivedConcept> readArchivesForGame(@PathVariable String gameId,
			@RequestParam(required = false) String playerId,
			@RequestParam(required = false) String state,
            @RequestParam(required = false) Long from, @RequestParam(required = false) Long to) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

        Date fromDate = null;
        Date toDate = null;
        if (from != null) {
            fromDate = new Date(from);
        }

        if (to != null) {
            toDate = new Date(to);
        }
        List<ArchivedConcept> result =
                archiveSrv.readArchives(gameId, playerId, state, fromDate, toDate);

		return result;

	}

}