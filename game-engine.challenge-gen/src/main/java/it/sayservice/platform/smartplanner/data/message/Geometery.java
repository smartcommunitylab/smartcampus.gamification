/**
 *    Copyright 2011-2013 SAYservice s.r.l.
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

package it.sayservice.platform.smartplanner.data.message;

import java.io.Serializable;


public class Geometery implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7857962270240302948L;

	/**
	 * @param type
	 * @param coordinates
	 * @param description
	 */
	public Geometery(String type, String coordinates, String description) {
		super();
		this.type = type;
		this.coordinates = coordinates;
		this.description = description;
	}

	private String type;
	private String coordinates;
	private String description;

	public Geometery(String type, String coordinates) {
		super();
		this.type = type;
		this.coordinates = coordinates;
	}

	public Geometery() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public String toString() {
		return "Geometery [type=" + type + ", coordinates=" + coordinates
				+ ", description=" + description + "]";
	}

	public Geometery setDescription(String valueAsText) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription() {
		return description;
	}
}