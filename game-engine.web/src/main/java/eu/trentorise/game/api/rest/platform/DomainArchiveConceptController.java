package eu.trentorise.game.api.rest.platform;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

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
@Profile("platform")
public class DomainArchiveConceptController {

	@Autowired
    private ArchiveManager archiveSrv;

    @RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/data/game/{gameId}/archive",
            produces = {
			"application/json" })
	@ApiOperation(value = "Read archive concepts for a game with optional filter parameters")
    public List<ArchivedConcept> readArchivesForGame(@PathVariable String domain,
            @PathVariable String gameId,
			@RequestParam(required = false) String playerId,
			@RequestParam(required = false) String state,
            @RequestParam(required = false) Long from, @RequestParam(required = false) Long to) {

        gameId = decodePathVariable(gameId);

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
