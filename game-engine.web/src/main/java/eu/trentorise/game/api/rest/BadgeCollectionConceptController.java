package eu.trentorise.game.api.rest;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.core.ResourceNotFoundException;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.utils.Converter;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@Profile({"sec", "no-sec"})
public class BadgeCollectionConceptController {

    @Autowired
    private GameService gameSrv;

    @Autowired
    private Converter converter;

    // Create badge collection concept
    // POST /model/game/{id}/badges

    @RequestMapping(method = RequestMethod.POST, value = "/model/game/{gameId}/badges",
            consumes = {"application/json"}, produces = {"application/json"})
    @Operation(summary = "Add a badge collection",
            description = "Add a badge collection to the game definition")
    public void addBadge(@PathVariable String gameId,
            @RequestBody BadgeCollectionConcept badge) {
        gameId = decodePathVariable(gameId);
        gameSrv.addConceptInstance(gameId, badge);
    }

    // Update badge collection concept
    // PUT /model/game/{id}/badges/{colllectionId}

    @RequestMapping(method = RequestMethod.PUT,
            value = "/model/game/{gameId}/badges/{collectionId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @Operation(summary = "Update a badge collection")
    public void updateBadgeCollection(@PathVariable String gameId) {
        gameId = decodePathVariable(gameId);

        throw new UnsupportedOperationException("Operation actually not supported");
    }

    // Read badge collection concepts
    // GET /model/game/{id}/badges

    @RequestMapping(method = RequestMethod.GET, value = "/model/game/{gameId}/badges",
            produces = {"application/json"})
    @Operation(summary = "Get the badge collections", description = "Get badge collections in a game")
    public List<BadgeCollectionConcept> readBadgeCollections(
            @PathVariable String gameId) {
        gameId = decodePathVariable(gameId);

        Set<GameConcept> concepts = gameSrv.readConceptInstances(gameId);
        List<BadgeCollectionConcept> badgeColl = new ArrayList<BadgeCollectionConcept>();
        if (concepts != null) {
            for (GameConcept gc : concepts) {
                if (gc instanceof BadgeCollectionConcept) {
                    badgeColl.add((BadgeCollectionConcept) gc);
                }
            }
        }
        return badgeColl;
    }

    // Read badge collection concept
    // GET /model/game/{id}/badges/{colllectionId}

    @RequestMapping(method = RequestMethod.GET,
            value = "/model/game/{gameId}/badges/{collectionId}",
            produces = {"application/json"})
    @Operation(summary = "Get a badge collection",
            description = "Get the definition of a badge collection in a game")
    public BadgeCollectionConcept readBadgeCollection(
            @PathVariable String gameId, @PathVariable String collectionId) {

        gameId = decodePathVariable(gameId);
        collectionId = decodePathVariable(collectionId);

        List<BadgeCollectionConcept> collection = readBadgeCollections(gameId);

        for (BadgeCollectionConcept c : collection) {
            if (collectionId.equals(c.getId())) {
                return c;
            }
        }
        throw new ResourceNotFoundException(
                String.format("BadgeCollectionId %s not exist in game %s", collectionId, gameId));
    }

    // Delete badge collection concept
    // DELETE /model/game/{id}/badges/{colllectionId}

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/model/game/{gameId}/badges/{collectionId}",
            produces = {"application/json"})
    @Operation(summary = "Delete a badge collection")
    public void deleteBadgeCollection(@PathVariable String gameId,
            @PathVariable String collectionId) {
        gameId = decodePathVariable(gameId);
        collectionId = decodePathVariable(collectionId);

        Game g = gameSrv.loadGameDefinitionById(gameId);
        if (g != null) {
            for (Iterator<GameConcept> iter = g.getConcepts().iterator(); iter.hasNext();) {
                GameConcept gc = iter.next();
                if (gc instanceof BadgeCollectionConcept && collectionId.equals(gc.getId())) {
                    iter.remove();
                    break;
                }
            }
            gameSrv.saveGameDefinition(g);
        }

    }
}
