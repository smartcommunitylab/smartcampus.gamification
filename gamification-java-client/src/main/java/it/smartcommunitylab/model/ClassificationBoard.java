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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.swagger.annotations.ApiModelProperty;

/**
 * ClassificationBoard
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-12-12T17:12:54.525+01:00")
public class ClassificationBoard {
  @SerializedName("board")
  private List<ClassificationPosition> board = null;

  @SerializedName("pointConceptName")
  private String pointConceptName = null;

  /**
   * Gets or Sets type
   */
  @JsonAdapter(TypeEnum.Adapter.class)
  public enum TypeEnum {
    GENERAL("GENERAL"),
    
    INCREMENTAL("INCREMENTAL");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<TypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TypeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return TypeEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("type")
  private TypeEnum type = null;

  public ClassificationBoard board(List<ClassificationPosition> board) {
    this.board = board;
    return this;
  }

  public ClassificationBoard addBoardItem(ClassificationPosition boardItem) {
    if (this.board == null) {
      this.board = new ArrayList<ClassificationPosition>();
    }
    this.board.add(boardItem);
    return this;
  }

   /**
   * Get board
   * @return board
  **/
  @ApiModelProperty(value = "")
  public List<ClassificationPosition> getBoard() {
    return board;
  }

  public void setBoard(List<ClassificationPosition> board) {
    this.board = board;
  }

  public ClassificationBoard pointConceptName(String pointConceptName) {
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

  public ClassificationBoard type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClassificationBoard classificationBoard = (ClassificationBoard) o;
    return Objects.equals(this.board, classificationBoard.board) &&
        Objects.equals(this.pointConceptName, classificationBoard.pointConceptName) &&
        Objects.equals(this.type, classificationBoard.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(board, pointConceptName, type);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClassificationBoard {\n");
    
    sb.append("    board: ").append(toIndentedString(board)).append("\n");
    sb.append("    pointConceptName: ").append(toIndentedString(pointConceptName)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
