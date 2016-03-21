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

/**
 * 
 * Geometry of a leg (used for graphical representation)
 */
public class LegGeometery implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1350145272450557941L;
	
	/**
	 * Lenght
	 */
	private long length;
	
	private String levels;
	
	/**
	 * Encoded path
	 */
	private String points;
	
	/**
	 * @param length
	 * @param levels
	 * @param points
	 */
	public LegGeometery(long length, String levels, String points) {
		super();
		this.length = length;
		this.levels = levels;
		this.points = points;
	}
	/**
	 * 
	 */
	public LegGeometery() {
		// TODO Auto-generated constructor stub
	}
	
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public String getLevels() {
		return levels;
	}
	public void setLevels(String levels) {
		this.levels = levels;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	
	
	@Override
	public String toString() {
		return "LegGeometery [length=" + length + ", levels=" + levels
				+ ", points=" + points + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (length ^ (length >>> 32));
		result = prime * result + ((levels == null) ? 0 : levels.hashCode());
		result = prime * result + ((points == null) ? 0 : points.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LegGeometery other = (LegGeometery) obj;
		if (length != other.length)
			return false;
		if (levels == null) {
			if (other.levels != null)
				return false;
		} else if (!levels.equals(other.levels))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		return true;
	}
	
}