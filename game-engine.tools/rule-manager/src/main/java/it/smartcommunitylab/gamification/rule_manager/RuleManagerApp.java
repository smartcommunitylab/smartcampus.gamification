package it.smartcommunitylab.gamification.rule_manager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.basic.api.RuleControllerApi;
import it.smartcommunitylab.gamification.rule_manager.CliOptions.Options;
import it.smartcommunitylab.model.RuleDTO;

public class RuleManagerApp {

	public static void main(String[] args) throws Exception {
		Scanner inputScanner = new Scanner(System.in);
		boolean idle = true;
		CliOptions options = new CliOptions(args);
		while (idle) {
			System.out.println("Command> ");
			String command = inputScanner.next();
			switch (command) {
			case "download":
				downloadCommand(options);
				break;
			case "update":
				updateCommand(options);
				break;
			case "exit":
				idle = false;
				break;
			default:
				System.out.println("Supported commands: download, update, exit");
				break;
			}

		}
		inputScanner.close();
		System.out.println("Done");
	}

	private static ApiClient client(CliOptions options) {
		ApiClient client = new ApiClient(options.get(Options.URL));
		client.setUsername(options.get(Options.USERNAME));
		client.setPassword(options.get(Options.PASSWORD));
		return client;
	}
	private static void updateCommand(CliOptions options) throws Exception {
		RuleControllerApi ruleApi = new RuleControllerApi(client(options));
		List<RuleDTO> rules = ruleApi.readAllRulesUsingGET(options.get(Options.GAME));
		Map<String, String> ruleMapping = new HashMap<String, String>();
		rules.forEach(rule -> {
			ruleMapping.put(rule.getName(), rule.getId());
		});
		Files.list(Path.of(options.get(Options.FROM))).map(ruleFilePath -> {
			String ruleName = ruleFilePath.toFile().getName().substring(0,
					ruleFilePath.toFile().getName().length() - 4);
			String ruleId = ruleMapping.get(ruleName);
			String content;
			RuleDTO ruleDTO = new RuleDTO();
			ruleDTO.setId(ruleId);
			ruleDTO.setName(ruleName);
			try {
				content = new String(Files.readAllBytes(ruleFilePath));
				ruleDTO.setContent(content);
			} catch (IOException e) {
				System.out.println("issue reading rule " + ruleName);
			}
			return ruleDTO;
		}).forEach(ruleDTO -> {
			try {
				ruleApi.editRuleUsingPUT(options.get(Options.GAME), ruleDTO.getId(), ruleDTO);
				System.out.println("updated rule " + ruleDTO.getName());
			} catch (ApiException e) {
				System.out.println("error updating rule " + ruleDTO.getName());
				try {
					ruleApi.addRuleUsingPOST1(options.get(Options.GAME), ruleDTO);
				} catch (ApiException e1) {
					System.out.println("error uploading new rule " + ruleDTO.getName());
				}
				System.out.println("updload new rule " + ruleDTO.getName());
			}
		});
	}

	private static void downloadCommand(CliOptions options) throws Exception {
		RuleControllerApi ruleApi = new RuleControllerApi(client(options));
		List<RuleDTO> rules = ruleApi.readAllRulesUsingGET(options.get(Options.GAME));
		Path outputFolderPath = Paths.get("rules", options.get(Options.GAME));
		Files.createDirectories(outputFolderPath);
		rules.stream().forEach(rule -> {
			ByteArrayInputStream contentStream = new ByteArrayInputStream(rule.getContent().getBytes());
			try {
				Files.copy(contentStream, outputFolderPath.resolve(rule.getName() + ".drl"));
				System.out.println("downloaded rule " + rule.getName());
			} catch (IOException e) {
				System.out.println("error downloading rule " + rule.getName());
			}
		});
	}
}
