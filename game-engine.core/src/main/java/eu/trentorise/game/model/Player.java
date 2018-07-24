/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.game.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
	private String id;
	private boolean team = false;
	private int totalMembers = 0;
    private Map<String, String> levels = new HashMap<>();

	public Player(String id) {
		this.id = id;
	}

	public Player(String id, boolean isTeam) {
		this.id = id;
		this.team = isTeam;
	}

	public Player(String id, boolean isTeam, int totalMembers) {
		this.id = id;
		this.team = isTeam;
		this.totalMembers = totalMembers;
	}

    public Player(PlayerState state) {
        if (state != null) {
            this.id = state.getPlayerId();
            this.team = state instanceof TeamState;
            if (team) {
                TeamState teamState = (TeamState) state;
                this.totalMembers = teamState.getMembers().size();
            }
            
            levels = convert(state.getLevels());
        }
    }

    private Map<String, String> convert(List<PlayerLevel> levels) {
        Map<String, String> mapLevels = new HashMap<>();
        if (levels != null) {
            levels.forEach(lev -> mapLevels.put(lev.getLevelName(), lev.getLevelValue()));
        }

        return mapLevels;
    }

	public String getId() {
		return id;
	}


	public boolean isTeam() {
		return team;
	}

	public int getTotalMembers() {
		return totalMembers;
	}

    public Map<String, String> getLevels() {
        return Collections.unmodifiableMap(levels);
    }

}
