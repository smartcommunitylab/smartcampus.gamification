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

/*
 * Gamification Engine API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package it.smartcommunitylab.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.threeten.bp.OffsetDateTime;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * PeriodInternal
 */

public class PeriodInternal {
  @SerializedName("capacity")
  private Integer capacity = null;

  @SerializedName("identifier")
  private String identifier = null;

  @SerializedName("instances")
  private Map<String, PeriodInstanceImpl> instances = null;

  @SerializedName("period")
  private Long period = null;

  @SerializedName("start")
  private Date start = null;

  public PeriodInternal capacity(Integer capacity) {
    this.capacity = capacity;
    return this;
  }

   /**
   * Get capacity
   * @return capacity
  **/
  @ApiModelProperty(value = "")
  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public PeriodInternal identifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

   /**
   * Get identifier
   * @return identifier
  **/
  @ApiModelProperty(value = "")
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public PeriodInternal instances(Map<String, PeriodInstanceImpl> instances) {
    this.instances = instances;
    return this;
  }

  public PeriodInternal putInstancesItem(String key, PeriodInstanceImpl instancesItem) {
    if (this.instances == null) {
      this.instances = new HashMap<String, PeriodInstanceImpl>();
    }
    this.instances.put(key, instancesItem);
    return this;
  }

   /**
   * Get instances
   * @return instances
  **/
  @ApiModelProperty(value = "")
  public Map<String, PeriodInstanceImpl> getInstances() {
    return instances;
  }

  public void setInstances(Map<String, PeriodInstanceImpl> instances) {
    this.instances = instances;
  }

  public PeriodInternal period(Long period) {
    this.period = period;
    return this;
  }

   /**
   * Get period
   * @return period
  **/
  @ApiModelProperty(value = "")
  public Long getPeriod() {
    return period;
  }

  public void setPeriod(Long period) {
    this.period = period;
  }

  public PeriodInternal start(Date start) {
    this.start = start;
    return this;
  }

   /**
   * Get start
   * @return start
  **/
  @ApiModelProperty(value = "")
  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PeriodInternal periodInternal = (PeriodInternal) o;
    return Objects.equals(this.capacity, periodInternal.capacity) &&
        Objects.equals(this.identifier, periodInternal.identifier) &&
        Objects.equals(this.instances, periodInternal.instances) &&
        Objects.equals(this.period, periodInternal.period) &&
        Objects.equals(this.start, periodInternal.start);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capacity, identifier, instances, period, start);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PeriodInternal {\n");
    
    sb.append("    capacity: ").append(toIndentedString(capacity)).append("\n");
    sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
    sb.append("    instances: ").append(toIndentedString(instances)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

