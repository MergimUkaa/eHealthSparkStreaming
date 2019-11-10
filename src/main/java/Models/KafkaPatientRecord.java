package Models;

public class KafkaPatientRecord implements java.io.Serializable {
    private Integer id;
    private Double minValue;
    private Double maxValue;
    private Double latitude;
    private Double longitude;

    public  KafkaPatientRecord(Integer id, Double minValue, Double maxValue, Double latitude, Double longitude) {
        this.id = id;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Integer getId(){
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMinValue() {
        return minValue;
    }
    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
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
}
