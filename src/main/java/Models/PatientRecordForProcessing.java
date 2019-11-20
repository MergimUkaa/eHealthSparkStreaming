package Models;

public class PatientRecordForProcessing implements java.io.Serializable {
    private Integer patientId;
    private Integer visitId;
    private Integer doctorId;
    private Integer sensorId;
    private Double parameterMinValue;
    private Double parameterMaxValue;
    private Double parameterNormalMinValue;
    private Double parameterNormalMaxValue;
    private Double latitude;
    private Double longitude;
    private String status;
    private String parameterUnit;

    public PatientRecordForProcessing(Integer patientId, Integer visitId, Integer doctorId, Integer sensorId, Double parameterMinValue, Double parameterMaxValue, Double parameterNormalMinValue, Double parameterNormalMaxValue, Double latitude, Double longitude, String parameterUnit) {
        this.patientId = patientId;
        this.visitId = visitId;
        this.doctorId = doctorId;
        this.sensorId = sensorId;
        this.parameterMinValue = parameterMinValue;
        this.parameterMaxValue = parameterMaxValue;
        this.parameterNormalMinValue = parameterNormalMinValue;
        this.parameterNormalMaxValue = parameterNormalMaxValue;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parameterUnit = parameterUnit;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public Double getParameterMinValue() {
        return parameterMinValue;
    }

    public void setParameterMinValue(Double parameterMinValue) {
        this.parameterMinValue = parameterMinValue;
    }

    public Double getParameterMaxValue() {
        return parameterMaxValue;
    }

    public void setParameterMaxValue(Double parameterMaxValue) {
        this.parameterMaxValue = parameterMaxValue;
    }

    public Double getParameterNormalMinValue() {
        return parameterNormalMinValue;
    }

    public void setParameterNormalMinValue(Double parameterNormalMinValue) {
        this.parameterNormalMinValue = parameterNormalMinValue;
    }

    public Double getParameterNormalMaxValue() {
        return parameterNormalMaxValue;
    }

    public void setParameterNormalMaxValue(Double parameterNormalMaxValue) {
        this.parameterNormalMaxValue = parameterNormalMaxValue;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParameterUnit() {
        return parameterUnit;
    }

    public void setParameterUnit(String parameterUnit) {
        this.parameterUnit = parameterUnit;
    }
}
