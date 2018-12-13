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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * GameStatistics
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-12-12T17:12:54.525+01:00")
public class GameStatistics {
  @SerializedName("average")
  private Double average = null;

  @SerializedName("endDate")
  private Long endDate = null;

  @SerializedName("gameId")
  private String gameId = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("lastUpdated")
  private Long lastUpdated = null;

  @SerializedName("periodIndex")
  private String periodIndex = null;

  @SerializedName("periodName")
  private String periodName = null;

  @SerializedName("pointConceptName")
  private String pointConceptName = null;

  @SerializedName("quantiles")
  private Map<String, Double> quantiles = null;

  @SerializedName("startDate")
  private Long startDate = null;

  @SerializedName("variance")
  private Double variance = null;

  public GameStatistics average(Double average) {
    this.average = average;
    return this;
  }

   /**
   * Get average
   * @return average
  **/
  @ApiModelProperty(value = "")
  public Double getAverage() {
    return average;
  }

  public void setAverage(Double average) {
    this.average = average;
  }

  public GameStatistics endDate(Long endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * Get endDate
   * @return endDate
  **/
  @ApiModelProperty(value = "")
  public Long getEndDate() {
    return endDate;
  }

  public void setEndDate(Long endDate) {
    this.endDate = endDate;
  }

  public GameStatistics gameId(String gameId) {
    this.gameId = gameId;
    return this;
  }

   /**
   * Get gameId
   * @return gameId
  **/
  @ApiModelProperty(value = "")
  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public GameStatistics id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GameStatistics lastUpdated(Long lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

   /**
   * Get lastUpdated
   * @return lastUpdated
  **/
  @ApiModelProperty(value = "")
  public Long getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Long lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public GameStatistics periodIndex(String periodIndex) {
    this.periodIndex = periodIndex;
    return this;
  }

   /**
   * Get periodIndex
   * @return periodIndex
  **/
  @ApiModelProperty(value = "")
  public String getPeriodIndex() {
    return periodIndex;
  }

  public void setPeriodIndex(String periodIndex) {
    this.periodIndex = periodIndex;
  }

  public GameStatistics periodName(String periodName) {
    this.periodName = periodName;
    return this;
  }

   /**
   * Get periodName
   * @return periodName
  **/
  @ApiModelProperty(value = "")
  public String getPeriodName() {
    return periodName;
  }

  public void setPeriodName(String periodName) {
    this.periodName = periodName;
  }

  public GameStatistics pointConceptName(String pointConceptName) {
    this.pointConceptName = pointConceptName;
    return this;
  }

   /**
   * Get pointConceptName
   * @return pointConceptName
  **/
  @ApiModelProperty(value = "")
  public String getPointConceptName() {
    return pointConceptName;
  }

  public void setPointConceptName(String pointConceptName) {
    this.pointConceptName = pointConceptName;
  }

  public GameStatistics quantiles(Map<String, Double> quantiles) {
    this.quantiles = quantiles;
    return this;
  }

  public GameStatistics putQuantilesItem(String key, Double quantilesItem) {
    if (this.quantiles == null) {
      this.quantiles = new HashMap<String, Double>();
    }
    this.quantiles.put(key, quantilesItem);
    return this;
  }

   /**
   * Get quantiles
   * @return quantiles
  **/
  @ApiModelProperty(value = "")
  public Map<String, Double> getQuantiles() {
    return quantiles;
  }

  public void setQuantiles(Map<String, Double> quantiles) {
    this.quantiles = quantiles;
  }

  public GameStatistics startDate(Long startDate) {
    this.startDate = startDate;
    return this;
  }

   /**
   * Get startDate
   * @return startDate
  **/
  @ApiModelProperty(value = "")
  public Long getStartDate() {
    return startDate;
  }

  public void setStartDate(Long startDate) {
    this.startDate = startDate;
  }

  public GameStatistics variance(Double variance) {
    this.variance = variance;
    return this;
  }

   /**
   * Get variance
   * @return variance
  **/
  @ApiModelProperty(value = "")
  public Double getVariance() {
    return variance;
  }

  public void setVariance(Double variance) {
    this.variance = variance;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameStatistics gameStatistics = (GameStatistics) o;
    return Objects.equals(this.average, gameStatistics.average) &&
        Objects.equals(this.endDate, gameStatistics.endDate) &&
        Objects.equals(this.gameId, gameStatistics.gameId) &&
        Objects.equals(this.id, gameStatistics.id) &&
        Objects.equals(this.lastUpdated, gameStatistics.lastUpdated) &&
        Objects.equals(this.periodIndex, gameStatistics.periodIndex) &&
        Objects.equals(this.periodName, gameStatistics.periodName) &&
        Objects.equals(this.pointConceptName, gameStatistics.pointConceptName) &&
        Objects.equals(this.quantiles, gameStatistics.quantiles) &&
        Objects.equals(this.startDate, gameStatistics.startDate) &&
        Objects.equals(this.variance, gameStatistics.variance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(average, endDate, gameId, id, lastUpdated, periodIndex, periodName, pointConceptName, quantiles, startDate, variance);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameStatistics {\n");
    
    sb.append("    average: ").append(toIndentedString(average)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    gameId: ").append(toIndentedString(gameId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    lastUpdated: ").append(toIndentedString(lastUpdated)).append("\n");
    sb.append("    periodIndex: ").append(toIndentedString(periodIndex)).append("\n");
    sb.append("    periodName: ").append(toIndentedString(periodName)).append("\n");
    sb.append("    pointConceptName: ").append(toIndentedString(pointConceptName)).append("\n");
    sb.append("    quantiles: ").append(toIndentedString(quantiles)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    variance: ").append(toIndentedString(variance)).append("\n");
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

