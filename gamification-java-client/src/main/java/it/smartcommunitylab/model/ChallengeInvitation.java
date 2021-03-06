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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.threeten.bp.OffsetDateTime;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * ChallengeInvitation
 */

public class ChallengeInvitation {
  @SerializedName("challengeEnd")
  private OffsetDateTime challengeEnd = null;

  @SerializedName("challengeModelName")
  private String challengeModelName = null;

  @SerializedName("challengeName")
  private String challengeName = null;

  @SerializedName("challengePointConcept")
  private PointConceptRef challengePointConcept = null;

  @SerializedName("challengeStart")
  private OffsetDateTime challengeStart = null;

  @SerializedName("challengeTarget")
  private Double challengeTarget = null;

  @SerializedName("gameId")
  private String gameId = null;

  @SerializedName("guests")
  private List<Player> guests = null;

  @SerializedName("proposer")
  private Player proposer = null;

  @SerializedName("reward")
  private Reward reward = null;

  public ChallengeInvitation challengeEnd(OffsetDateTime challengeEnd) {
    this.challengeEnd = challengeEnd;
    return this;
  }

   /**
   * Get challengeEnd
   * @return challengeEnd
  **/
  @ApiModelProperty(value = "")
  public OffsetDateTime getChallengeEnd() {
    return challengeEnd;
  }

  public void setChallengeEnd(OffsetDateTime challengeEnd) {
    this.challengeEnd = challengeEnd;
  }

  public ChallengeInvitation challengeModelName(String challengeModelName) {
    this.challengeModelName = challengeModelName;
    return this;
  }

   /**
   * Get challengeModelName
   * @return challengeModelName
  **/
  @ApiModelProperty(value = "")
  public String getChallengeModelName() {
    return challengeModelName;
  }

  public void setChallengeModelName(String challengeModelName) {
    this.challengeModelName = challengeModelName;
  }

  public ChallengeInvitation challengeName(String challengeName) {
    this.challengeName = challengeName;
    return this;
  }

   /**
   * Get challengeName
   * @return challengeName
  **/
  @ApiModelProperty(value = "")
  public String getChallengeName() {
    return challengeName;
  }

  public void setChallengeName(String challengeName) {
    this.challengeName = challengeName;
  }

  public ChallengeInvitation challengePointConcept(PointConceptRef challengePointConcept) {
    this.challengePointConcept = challengePointConcept;
    return this;
  }

   /**
   * Get challengePointConcept
   * @return challengePointConcept
  **/
  @ApiModelProperty(value = "")
  public PointConceptRef getChallengePointConcept() {
    return challengePointConcept;
  }

  public void setChallengePointConcept(PointConceptRef challengePointConcept) {
    this.challengePointConcept = challengePointConcept;
  }

  public ChallengeInvitation challengeStart(OffsetDateTime challengeStart) {
    this.challengeStart = challengeStart;
    return this;
  }

   /**
   * Get challengeStart
   * @return challengeStart
  **/
  @ApiModelProperty(value = "")
  public OffsetDateTime getChallengeStart() {
    return challengeStart;
  }

  public void setChallengeStart(OffsetDateTime challengeStart) {
    this.challengeStart = challengeStart;
  }

  public ChallengeInvitation challengeTarget(Double challengeTarget) {
    this.challengeTarget = challengeTarget;
    return this;
  }

   /**
   * Get challengeTarget
   * @return challengeTarget
  **/
  @ApiModelProperty(value = "")
  public Double getChallengeTarget() {
    return challengeTarget;
  }

  public void setChallengeTarget(Double challengeTarget) {
    this.challengeTarget = challengeTarget;
  }

  public ChallengeInvitation gameId(String gameId) {
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

  public ChallengeInvitation guests(List<Player> guests) {
    this.guests = guests;
    return this;
  }

  public ChallengeInvitation addGuestsItem(Player guestsItem) {
    if (this.guests == null) {
      this.guests = new ArrayList<Player>();
    }
    this.guests.add(guestsItem);
    return this;
  }

   /**
   * Get guests
   * @return guests
  **/
  @ApiModelProperty(value = "")
  public List<Player> getGuests() {
    return guests;
  }

  public void setGuests(List<Player> guests) {
    this.guests = guests;
  }

  public ChallengeInvitation proposer(Player proposer) {
    this.proposer = proposer;
    return this;
  }

   /**
   * Get proposer
   * @return proposer
  **/
  @ApiModelProperty(value = "")
  public Player getProposer() {
    return proposer;
  }

  public void setProposer(Player proposer) {
    this.proposer = proposer;
  }

  public ChallengeInvitation reward(Reward reward) {
    this.reward = reward;
    return this;
  }

   /**
   * Get reward
   * @return reward
  **/
  @ApiModelProperty(value = "")
  public Reward getReward() {
    return reward;
  }

  public void setReward(Reward reward) {
    this.reward = reward;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChallengeInvitation challengeInvitation = (ChallengeInvitation) o;
    return Objects.equals(this.challengeEnd, challengeInvitation.challengeEnd) &&
        Objects.equals(this.challengeModelName, challengeInvitation.challengeModelName) &&
        Objects.equals(this.challengeName, challengeInvitation.challengeName) &&
        Objects.equals(this.challengePointConcept, challengeInvitation.challengePointConcept) &&
        Objects.equals(this.challengeStart, challengeInvitation.challengeStart) &&
        Objects.equals(this.challengeTarget, challengeInvitation.challengeTarget) &&
        Objects.equals(this.gameId, challengeInvitation.gameId) &&
        Objects.equals(this.guests, challengeInvitation.guests) &&
        Objects.equals(this.proposer, challengeInvitation.proposer) &&
        Objects.equals(this.reward, challengeInvitation.reward);
  }

  @Override
  public int hashCode() {
    return Objects.hash(challengeEnd, challengeModelName, challengeName, challengePointConcept, challengeStart, challengeTarget, gameId, guests, proposer, reward);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeInvitation {\n");
    
    sb.append("    challengeEnd: ").append(toIndentedString(challengeEnd)).append("\n");
    sb.append("    challengeModelName: ").append(toIndentedString(challengeModelName)).append("\n");
    sb.append("    challengeName: ").append(toIndentedString(challengeName)).append("\n");
    sb.append("    challengePointConcept: ").append(toIndentedString(challengePointConcept)).append("\n");
    sb.append("    challengeStart: ").append(toIndentedString(challengeStart)).append("\n");
    sb.append("    challengeTarget: ").append(toIndentedString(challengeTarget)).append("\n");
    sb.append("    gameId: ").append(toIndentedString(gameId)).append("\n");
    sb.append("    guests: ").append(toIndentedString(guests)).append("\n");
    sb.append("    proposer: ").append(toIndentedString(proposer)).append("\n");
    sb.append("    reward: ").append(toIndentedString(reward)).append("\n");
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

