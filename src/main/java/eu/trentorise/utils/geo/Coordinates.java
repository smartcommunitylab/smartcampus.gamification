package eu.trentorise.utils.geo;

/**
 *
 * @author Luca Piras
 */
public class Coordinates {
    
    protected Double latitude;
    protected Double longitude;
    
    protected Integer x;
    protected Integer y;

    public Boolean areGeoCoordinatesComplete() {
        return (null != latitude && null != longitude);
    }
    
    public Boolean areCartesianCoordinatesComplete() {
        return (null != x && null != y);
    }
    
    public Object getFirstCoordinate() {
        Object objectToReturn = null;
        
        if (areGeoCoordinatesComplete()) {
            objectToReturn = latitude;
        } else if (areCartesianCoordinatesComplete()) {
            objectToReturn = x;
        }
        
        return objectToReturn;
    }
    
    public Object getSecondCoordinate() {
        Object objectToReturn = null;
        
        if (areGeoCoordinatesComplete()) {
            objectToReturn = longitude;
        } else if (areCartesianCoordinatesComplete()) {
            objectToReturn = y;
        }
        
        return objectToReturn;
    }
    
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.latitude != null ? this.latitude.hashCode() : 0);
        hash = 89 * hash + (this.longitude != null ? this.longitude.hashCode() : 0);
        hash = 89 * hash + (this.x != null ? this.x.hashCode() : 0);
        hash = 89 * hash + (this.y != null ? this.y.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordinates other = (Coordinates) obj;
        if (this.latitude != other.latitude && (this.latitude == null || !this.latitude.equals(other.latitude))) {
            return false;
        }
        if (this.longitude != other.longitude && (this.longitude == null || !this.longitude.equals(other.longitude))) {
            return false;
        }
        if (this.x != other.x && (this.x == null || !this.x.equals(other.x))) {
            return false;
        }
        if (this.y != other.y && (this.y == null || !this.y.equals(other.y))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Coordinates{" + "latitude=" + latitude + ", longitude=" + longitude + ", x=" + x + ", y=" + y + '}';
    }
}