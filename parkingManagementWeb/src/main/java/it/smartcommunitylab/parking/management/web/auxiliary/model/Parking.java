/*******************************************************************************
 * Copyright 2015 Fondazione Bruno Kessler
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
 ******************************************************************************/
package it.smartcommunitylab.parking.management.web.auxiliary.model;

import java.io.Serializable;

public class Parking implements Serializable  {
	private static final long serialVersionUID = 8076535734041609036L;

	// From GeoObject
	private String id;
	private String agency;
	private double[] position;
	private String name;
	private String description;
	// Fields from basic object
	private Long updateTime;
	private Long version;
	private Integer user;
	
	private int slotsTotal;
	private int slotsOccupiedOnTotal;
	private int slotsUnavailable;
	private LastChange lastChange;
	
	public int getSlotsTotal() {
		return slotsTotal;
	}

	public int getSlotsOccupiedOnTotal() {
		return slotsOccupiedOnTotal;
	}

	public void setSlotsOccupiedOnTotal(int mSlotsOccupiedOnTotal) {
		this.slotsOccupiedOnTotal = mSlotsOccupiedOnTotal;
	}

	public int getSlotsUnavailable() {
		return slotsUnavailable;
	}

	public void setSlotsUnavailable(int mSlotsUnavailable) {
		this.slotsUnavailable = mSlotsUnavailable;
	}

	public LastChange getLastChange() {
		return lastChange;
	}

	public void setSlotsTotal(int slotsTotal) {
		this.slotsTotal = slotsTotal;
	}

	public void setLastChange(LastChange lastChange) {
		this.lastChange = lastChange;
	}
	
	
	public String getId() {
		return id;
	}

	public String getAgency() {
		return agency;
	}

	public double[] getPosition() {
		return position;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public Long getVersion() {
		return version;
	}

	public Integer getUser() {
		return user;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public void setPosition(double[] position) {
		this.position = position;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public String toJSON() {
		String json = "{";
		json += "\"id\":\"" + getId() + "\",";
		json += "\"updateTime\":" + getUpdateTime() + ",";
		json += "\"version\":" + getVersion() + ",";
		json += "\"user\":\"" + getUser() + "\",";
		json += "\"agency\":\"" + getAgency() + "\",";
		if(getPosition() != null && getPosition().length > 0){
			double[] pos = getPosition();
			json += "\"position\":[" + pos[0] +  "," + pos[1] + "],";		
		} else {
			json += "\"position\":[],";
		}
		json += "\"name\":\"" + getName() + "\",";
		json += "\"description\":\"" + getDescription() + "\",";
		json += "\"slotsTotal\":" + getSlotsTotal() + ",";
		json += "\"slotsOccupiedOnTotal\":" + getSlotsOccupiedOnTotal() + ",";
		json += "\"slotsUnavailable\":" + getSlotsUnavailable() + ",";
		json += "\"lastChange\":" + getUpdateTime() + "";
		json += "}";
		return json;
	}
}