package eu.trentorise.game.api.rest.platform;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.core.ResourceNotFoundException;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.utils.Converter;
import io.swagger.annotations.ApiOperation;

@RestController
@Profile("platform")
public class DomainPointConceptController {

    @Autowired
    private GameService gameSrv;

    @Autowired
    private Converter converter;

    // Create game point concept
    // POST /model/game/{id}/point
    // ­ Response body should contain point concept id
    // ­ May contain the periodic point definitions

    @RequestMapping(method = RequestMethod.POST, value = "/api/{domain}/model/game/{gameId}/point",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Add point")
    public PointConcept addPoint(@PathVariable String domain, @PathVariable String gameId,
            @RequestBody PointConcept point) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        point.setId(UUID.randomUUID().toString());

        gameSrv.addConceptInstance(gameId, point);
        return point;
    }

    // Update game point concept
    // PUT /model/game/{id}/point/{pointId}
    // ­ May contain the periodic point definitions

    @RequestMapping(method = RequestMethod.PUT,
            value = "/api/{domain}/model/game/{gameId}/point/{pointId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Edit point")
    public void updatePoint(@PathVariable String domain, @PathVariable String gameId,
            @RequestBody PointConcept point) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        throw new UnsupportedOperationException("Operation actually not supported");
    }

    // Read game point concepts
    // GET /model/game/{id}/point

    @RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/model/game/{gameId}/point",
            produces = {"application/json"})
    @ApiOperation(value = "Get points")
    public List<PointConcept> readPoints(@PathVariable String domain, @PathVariable String gameId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        Set<GameConcept> concepts = gameSrv.readConceptInstances(gameId);
        List<PointConcept> points = new ArrayList<PointConcept>();
        if (concepts != null) {
            for (GameConcept gc : concepts) {
                if (gc instanceof PointConcept) {
                    points.add((PointConcept) gc);
                }
            }
        }

        return points;
    }

    // Read game point concept
    // GET /model/game/{id}/point/{pointId}

    @RequestMapping(method = RequestMethod.GET,
            value = "/api/{domain}/model/game/{gameId}/point/{pointId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get point")
    public PointConcept readPoint(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String pointId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            pointId = URLDecoder.decode(pointId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("pointId is not UTF-8 encoded");
        }

        List<PointConcept> points = readPoints(domain, gameId);

        for (PointConcept point : points) {
            if (pointId.equals(point.getId())) {
                return point;
            }
        }
        throw new ResourceNotFoundException(
                String.format("pointId %s not exist in game %s", pointId, gameId));
    }

    // Delete game point concept
    // DELETE /model/game/{id}/point/{pointId}

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/api/{domain}/model/game/{gameId}/point/{pointId}",
            produces = {"application/json"})
    @ApiOperation(value = "Delete point")
    public void deletePoint(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String pointId) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            pointId = URLDecoder.decode(pointId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("pointId is not UTF-8 encoded");
        }

        Game g = gameSrv.loadGameDefinitionById(gameId);
        if (g != null) {
            for (Iterator<GameConcept> iter = g.getConcepts().iterator(); iter.hasNext();) {
                GameConcept gc = iter.next();
                if (gc instanceof PointConcept && pointId.equals(gc.getId())) {
                    iter.remove();
                    break;
                }
            }
            gameSrv.saveGameDefinition(g);
        }

    }

}
