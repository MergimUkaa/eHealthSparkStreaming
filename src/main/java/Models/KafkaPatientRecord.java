package Models;

public class KafkaPatientRecord implements java.io.Serializable {
    private Integer id;
    private Double minValue;
    private Double maxValue;

    public  KafkaPatientRecord(Integer id, Double minValue, Double maxValue) {
        this.id = id;
        this.minValue = minValue;
        this.maxValue = maxValue;
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
}
