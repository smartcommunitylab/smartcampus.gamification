package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;

import eu.trentorise.game.model.core.ChallengeAssignment;

public class ChallengeModel {

	@Id
	private String id;

	private String name;
	private Set<String> variables = new HashSet<>();

	private String gameId;


    public List<String> invalidFields(ChallengeUpdate changes) {
        return invalidFields(changes.getFields());
    }

    public List<String> invalidFields(ChallengeAssignment assignment) {
        return invalidFields(assignment.getData());
    }

    private List<String> invalidFields(Map<String, Object> fields) {
        List<String> invalidFields = new ArrayList<>();

        if (fields == null) {
            return Collections.emptyList();
        }
        for (String var : fields.keySet()) {
            if (!variables.contains(var)) {
                invalidFields.add(var);
            }
        }
        return invalidFields;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getVariables() {
		return variables;
	}

	public void setVariables(Set<String> variables) {
		this.variables = variables;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

}
