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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import eu.trentorise.game.model.core.GameConcept;

public class BadgeCollectionConcept extends GameConcept {

	private List<String> badgeEarned;
	private boolean hidden;

	public BadgeCollectionConcept() {
		badgeEarned = new ArrayList<String>();
	}

	public BadgeCollectionConcept(String name) {
		super(name);
		badgeEarned = new ArrayList<String>();
	}

	public List<String> getBadgeEarned() {
		return badgeEarned;
	}

	public void setBadgeEarned(List<String> badgeEarned) {
		this.badgeEarned = badgeEarned;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BadgeCollectionConcept) {
			BadgeCollectionConcept toCompare = (BadgeCollectionConcept) obj;
			return toCompare == this || name.equals(toCompare.name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(13, 23).append(name).hashCode();
	}

	@Override
	public String toString() {
		return String.format("{name:%s, badges: %s}", name, badgeEarned);
	}
}
