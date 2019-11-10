package Helpers;

import Models.PatientRecordForProcessing;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Helper {
    /**
     * Get patient data from stored procedure in MySQL and save to the list
     * @param sensorRDD
     * @return List
     */
    public static ArrayList<PatientRecordForProcessing> patientList(String sensorRDD) {
//        ResultSet rs = null;
        ArrayList <PatientRecordForProcessing> patientList = new ArrayList<>();
        try(Connection conn = MySQLConnection.getConnection()) {
            CallableStatement callableStatement = conn.prepareCall("{call getDataForProcessing(?)}");
            callableStatement.setString(1, getPatientIds(sensorRDD));
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                patientList.add(
                        new PatientRecordForProcessing(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getDouble(5),
                        rs.getDouble(6),
                        rs.getDouble(7),
                        rs.getDouble(8),
                        rs.getDouble(9),
                        rs.getDouble(10)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientList;
    }

    /**
     * getting each id of sensor and append as string with comma (,)
     * @param sensorRDD
     * @return String
     */
    public static String getPatientIds(String sensorRDD){
        Integer length = JsonParser.ParseKafkaData(sensorRDD).size();
        StringBuilder ids = new StringBuilder("");
        for (int i = 0; i < length; i++) {
            ids.append(JsonParser.ParseKafkaData(sensorRDD).get(i).getId());
            if (i != length - 1){
                ids.append(",");
            }
        }
        return  ids.toString();
    }
}
