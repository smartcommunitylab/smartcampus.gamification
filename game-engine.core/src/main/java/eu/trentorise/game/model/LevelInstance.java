package eu.trentorise.game.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class LevelInstance {
    private String type;
    private String name;


    public LevelInstance() {}



    public LevelInstance(String type, String name) {
        super();
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        LevelInstance rhs = (LevelInstance) obj;
        return new EqualsBuilder().append(type, rhs.type).append(name, rhs.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(31, 25).append(type).append(name).hashCode();
    }
}
