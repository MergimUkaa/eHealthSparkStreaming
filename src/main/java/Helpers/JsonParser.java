package Helpers;

import Models.KafkaPatientRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
    public static ArrayList<KafkaPatientRecord> ParseKafkaData(String sensorRDD) {
        //saving to the list each sensor of patient
        ArrayList <KafkaPatientRecord> patientList = new ArrayList<>();
        JSONArray sensorDataParsed = new JSONArray(sensorRDD);
        for (int i = 0; i<sensorDataParsed.length(); i++) {
            JSONObject patient = sensorDataParsed.getJSONObject(i);
            JSONArray sensorValues = patient.getJSONArray("SensorValues");
            JSONArray geoLocations = patient.getJSONArray("GeoLocations");
            patientList.add(
                    new KafkaPatientRecord(
                            patient.getInt("SensorId"),
                            sensorValues.getDouble(0),sensorValues.getDouble(1),
                            geoLocations.getDouble(0), sensorValues.getDouble(1)));
        }
        return patientList;
    }
}
