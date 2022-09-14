package eu.trentorise.game.api.rest;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.managers.ChallengeManager;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeReportCSVStub;
import eu.trentorise.game.model.ExportCsv;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.repo.ChallengeConceptPersistence;
import eu.trentorise.game.repo.ChallengeConceptRepo;
import eu.trentorise.game.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@RestController
public class ChallengeController {

	private static final Logger logger = LoggerFactory.getLogger(ChallengeController.class);

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private ChallengeManager challengeSrv;

	@Autowired
	private ChallengeConceptRepo challengeConceptRepo;

	@DeleteMapping("/data/game/{gameId}/player/{playerId}/challenge/{instanceName}")
	public ChallengeConcept deleteChallenge(@PathVariable String gameId, @PathVariable String playerId,
			@PathVariable String instanceName) {
		gameId = decodePathVariable(gameId);
		final String decodedPlayerId = decodePathVariable(playerId);
		final String decodedInstanceName = decodePathVariable(instanceName);

		PlayerState state = playerSrv.loadState(gameId, playerId, false, false);
		List<ChallengeConceptPersistence> listCcs = challengeConceptRepo.findByGameIdAndPlayerId(gameId, playerId);
		state.loadChallengeConcepts(listCcs);
		Optional<ChallengeConcept> removed = state.removeChallenge(decodedInstanceName);
		ChallengeConceptPersistence saved = challengeConceptRepo.findByGameIdAndPlayerIdAndName(gameId, playerId,
				instanceName);
		challengeConceptRepo.delete(saved);
		if (removed.isPresent()) {
			playerSrv.saveState(state);
			LogHub.info(gameId, logger, "removed challenge {} of player {}", instanceName, playerId);
		}
		return removed.orElseThrow(() -> new IllegalArgumentException(String
				.format("challenge %s doesn't exist in state of player %s", decodedInstanceName, decodedPlayerId)));
	}

	
	@RequestMapping(method = RequestMethod.GET, value = "/data/game/{gameId}/challenge/report", produces = {
			"application/json" })
	@Operation(summary = "Read single group challenge report for a game with optional filter parameters")
	@Parameters({
		@Parameter(name = "fileType", description = "csv|json "),
		@Parameter(name = "challengeType", description = "group|single")
		})
	public void getChallengeReportForGame(@PathVariable String gameId,
			@RequestParam(required = false) Long from,
			@RequestParam(required = false) Long to,
			@RequestParam(required = false) String challengeType,
			@RequestParam(required = false) String modelName,
			@RequestParam(required = false) Boolean hidden,
			@RequestParam String fileType,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		gameId = decodePathVariable(gameId);

		Date fromDate = null;
		Date toDate = null;
		if (from != null) {
			fromDate = new Date(from);
		}

		if (to != null) {
			toDate = new Date(to);
		}

		if (fileType.equalsIgnoreCase("csv")) {
			List<ChallengeReportCSVStub> result = challengeSrv.readChallengeReportCSV(gameId, fromDate, toDate,
					challengeType, modelName, hidden);
			ExportCsv reportCsv = challengeSrv.getChallengeReportCSV(result, gameId);
			response.setContentType(reportCsv.getContentType());
			response.setHeader("Content-Disposition", "attachment; filename=" + reportCsv.getFilename());
			response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
			response.getOutputStream().write(reportCsv.getContent().toString().getBytes(StandardCharsets.UTF_8));
		} else if (fileType.equalsIgnoreCase("json")) {
			String reportJSON = challengeSrv.readChallengeReportJSON(gameId, fromDate, toDate, challengeType, modelName,
					hidden);
			DateTimeFormatter ldf = DateTimeFormatter.ofPattern("dd-MM-YYYY");
			LocalDate today = LocalDate.now();
			String filename = "report_challenges" + "_" + gameId + "_" + today.format(ldf) + ".json";
			response.setContentType("application/json");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
			response.getOutputStream().write(reportJSON.getBytes(StandardCharsets.UTF_8));
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file type not specified");
		}
				
		if (logger.isInfoEnabled()) {
			logger.info(String.format("getChallengeReportForGame:%s", gameId));
		}

	}
	
}
