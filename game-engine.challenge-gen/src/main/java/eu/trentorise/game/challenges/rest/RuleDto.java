package eu.trentorise.game.challenges.rest;

/**
 * Simple Dto mean to be used for inserting rules
 */
public class RuleDto {

    private String id;
    private String content;
    private String name;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}
