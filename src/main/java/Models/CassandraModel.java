package Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class CassandraModel implements java.io.Serializable {
    private UUID id;
    private Integer doctor_id;
    private Integer patient_id;
    private Integer sensor_id;
    private Integer visit_id;
    private Double latitude;
    private Double longitude;
    private Double min_value;
    private Double max_value;
    private Double normal_max_value;
    private Double normal_min_value;
    private String parameter_unit;
    private String status;
    private Double min_value_measured;
    private Double max_value_measured;
    private Date created_at;
    private Date updated_at;

    public CassandraModel(
            UUID id,
            Integer doctor_id,
            Integer patient_id,
            Integer sensor_id,
            Integer visit_id,
            Double latitude,
            Double longitude,
            Double min_value,
            Double max_value,
            Double normal_max_value,
            Double normal_min_value,
            String parameter_unit,
            String status,
            Double min_value_measured,
            Double max_value_measured
            )

    {
        this.id = id;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.sensor_id = sensor_id;
        this.visit_id = visit_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.min_value = min_value;
        this.max_value = max_value;
        this.normal_max_value = normal_max_value;
        this.normal_min_value = normal_min_value;
        this.parameter_unit = parameter_unit;
        this.status = status;
        this.min_value_measured = min_value_measured;
        this.max_value_measured = max_value_measured;
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.created_at = new Date();
        this.updated_at = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Double getMin_value() {
        return min_value;
    }

    public void setMin_value(Double min_value) {
        this.min_value = min_value;
    }

    public Double getMax_value() {
        return max_value;
    }

    public void setMax_value(Double max_value) {
        this.max_value = max_value;
    }

    public Double getNormal_max_value() {
        return normal_max_value;
    }

    public void setNormal_max_value(Double normal_max_value) {
        this.normal_max_value = normal_max_value;
    }

    public Double getNormal_min_value() {
        return normal_min_value;
    }

    public void setNormal_min_value(Double normal_min_value) {
        this.normal_min_value = normal_min_value;
    }

    public String getParameter_unit() {
        return parameter_unit;
    }

    public void setParameter_unit(String parameter_unit) {
        this.parameter_unit = parameter_unit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

    public Integer getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(Integer sensor_id) {
        this.sensor_id = sensor_id;
    }

    public Integer getVisit_id() {
        return visit_id;
    }

    public void setVisit_id(Integer visit_id) {
        this.visit_id = visit_id;
    }

    public Double getMin_value_measured() {
        return min_value_measured;
    }

    public void setMin_value_measured(Double min_value_measured) {
        this.min_value_measured = min_value_measured;
    }

    public Double getMax_value_measured() {
        return max_value_measured;
    }

    public void setMax_value_measured(Double max_value_measured) {
        this.max_value_measured = max_value_measured;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }


    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
