package eu.trentorise.game.api.rest;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.trentorise.game.bean.RuleDTO;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import io.swagger.annotations.ApiOperation;

@RestController
@Profile({"sec", "no-sec"})
public class RuleController {

    @Autowired
    private GameService gameSrv;

    @Autowired
    private GameEngine gameEngine;

    // Create rule
    // POST /model/game/{id}/rule
    @RequestMapping(method = RequestMethod.POST, value = "/model/game/{gameId}/rule",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Add rule")
    public RuleDTO addRule(@PathVariable String gameId,
            @RequestBody RuleDTO rule) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        DBRule r = new DBRule(gameId, rule.getContent());
        r.setName(rule.getName());
        r.setId(rule.getId());
        String ruleUrl = gameSrv.addRule(r);
        rule.setId(ruleUrl);
        return rule;
    }

    // Update rule
    // PUT /model/game/{id}/rule/{ruleId}
    @RequestMapping(method = RequestMethod.PUT,
            value = "/model/game/{gameId}/rule/{ruleId}", consumes = {"application/json"},
            produces = {"application/json"})
    @ApiOperation(value = "Edit rule")
	public RuleDTO editRule(@PathVariable String gameId, @PathVariable String ruleId, @RequestBody RuleDTO rule) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        DBRule r = new DBRule(gameId, rule.getContent());
        r.setName(rule.getName());
        r.setId(rule.getId());
        String ruleUrl = gameSrv.addRule(r);
        rule.setId(ruleUrl);
        return rule;
    }

    // Read all rules
    // GET /model/game/{id}/rule
    @RequestMapping(method = RequestMethod.GET, value = "/model/game/{gameId}/rule",
            produces = {"application/json"})
    @ApiOperation(value = "Get rules")
    public List<RuleDTO> readAllRules(@PathVariable String gameId) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }
        Game g = gameSrv.loadGameDefinitionById(gameId);
        List<RuleDTO> rules = new ArrayList<>();
        if (g != null) {
            for (String ruleUrl : g.getRules()) {
                DBRule r = (DBRule) gameSrv.loadRule(gameId, ruleUrl);
                RuleDTO res = new RuleDTO();
                res.setId(r.getId());
                res.setName(r.getName());
                res.setContent(r.getContent());
                rules.add(res);
            }
        }
        return rules;
    }

    // Read a rule
    // GET /model/game/{id}/rule/{ruleId}
    @RequestMapping(method = RequestMethod.GET,
            value = "/model/game/{gameId}/rule/{ruleId}", produces = {"application/json"})
    @ApiOperation(value = "Get rule")
    public RuleDTO readDbRule(@PathVariable String gameId,
            @PathVariable String ruleId) {
        gameId = decodePathVariable(gameId);
        ruleId = DBRule.URL_PROTOCOL + ruleId;
        DBRule r = (DBRule) gameSrv.loadRule(gameId, ruleId);
        RuleDTO res = new RuleDTO();
        res.setId(r.getId());
        res.setName(r.getName());
        res.setContent(r.getContent());
        return res;
    }

    // Delete a rule
    // DELETE /model/game/{id}/rule/{ruleId}

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/model/game/{gameId}/rule/{ruleId}", produces = {"application/json"})
    @ApiOperation(value = "Delete rule")
    public boolean deleteDbRule(@PathVariable String gameId,
            @PathVariable String ruleId) {
        gameId = decodePathVariable(gameId);
        ruleId = DBRule.URL_PROTOCOL + ruleId;
        return gameSrv.deleteRule(gameId, ruleId);
    }

    // Validate a rule
    // POST /model/game/{id}/rule/validate
    // ­ Rule wrapped in an object {rule: “<rule text>”}

    @RequestMapping(method = RequestMethod.POST, value = "/model/game/{gameId}/rule/validate",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Validate rule")
    public List<String> validateRule(@PathVariable String gameId,
            @RequestBody RuleValidateWrapper wrapper) {
        return gameEngine.validateRule(gameId, wrapper.getRuleContent());
    }

    private class RuleValidateWrapper {

        @JsonProperty(value = "rule")
        private String ruleContent;

        public String getRuleContent() {
            return ruleContent;
        }

        public void setRuleContent(String ruleContent) {
            this.ruleContent = ruleContent;
        }

    }
}
