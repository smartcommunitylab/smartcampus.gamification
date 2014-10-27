package eu.trentorise.game.api.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.services.Workflow;

@RestController
public class MainController {

	@Autowired
	Workflow gameWorkflow;

	@RequestMapping(method = RequestMethod.POST, value = "/execute")
	public void executeAction(@RequestBody ExecutionData data) {
		gameWorkflow
				.apply(data.getActionId(), data.getUserId(), data.getData());
	}
}

class ExecutionData {
	private String actionId;
	private String userId;
	private Map<String, Object> data;

	public ExecutionData() {
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}