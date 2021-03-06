/**
 * Copyright 2018-2019 SmartCommunity Lab(FBK-ICT).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package it.smartcommunitylab.model.ext;

import it.smartcommunitylab.model.Notification;

public class ClassificationNotification extends Notification {
	private String classificationName;
	private int classificationPosition;

	public String getClassificationName() {
		return classificationName;
	}

	public void setClassificationName(String classificationName) {
		this.classificationName = classificationName;
	}

	public int getClassificationPosition() {
		return classificationPosition;
	}

	public void setClassificationPosition(int classificationPosition) {
		this.classificationPosition = classificationPosition;
	}

	@Override
	public String toString() {
		return String.format("[gameId=%s, playerId=%s, classificationName=%s classificationPosition=%s]", getGameId(),
				getPlayerId(), classificationName, classificationPosition);
	}

}
