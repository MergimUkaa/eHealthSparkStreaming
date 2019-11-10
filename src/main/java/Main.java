import Helpers.Helper;
import Models.PatientRecordForProcessing;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

//import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
//import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

public class Main {
    private static final Pattern SPACE = Pattern.compile(" ");
    public static void main(String[] args) throws InterruptedException {

//        System.setProperty("hadoop.home.dir", "\\\"C:\\\\winutils\\\"");
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("e-health streaming");

        // Create a StreamingContext with a 1-second batch size from a SparkConf
        JavaStreamingContext streamingContext = new JavaStreamingContext(
                sparkConf, Durations.seconds(10));


        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);


        Collection<String> topics = Arrays.asList("sensor-generator");


        JavaInputDStream<ConsumerRecord<String, String>> messages =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String> Subscribe(topics, kafkaParams));
        JavaDStream<String> lines = messages.map(ConsumerRecord::value);
        lines.foreachRDD(rdd -> {
            if (!rdd.isEmpty()) {

                // get each rdd
                String sensorRDD = rdd.collect().get(0);
//                JavaRDD<PatientRecordForProcessing> patientList = streamingContext.sparkContext().parallelize(Helper.patientList(sensorRDD));
                Helper.patientList(sensorRDD).forEach(patient -> {
                    patient.setStatus("critical normal max");
                    System.out.println("Doctor ID: " + patient.getDoctorId());
                    System.out.println("PatientID: " + patient.getPatientId());
                    System.out.println("Visit ID: " + patient.getVisitId());
                    System.out.println("Sensor ID: " + patient.getSensorId());
                    System.out.println("Min value: " + patient.getParameterMinValue());
                    System.out.println("Max value: " + patient.getParameterMaxValue());
                    System.out.println("Normal min value: " + patient.getParameterNormalMinValue());
                    System.out.println("Normal max value: " + patient.getParameterNormalMaxValue());
                    System.out.println("Latitude: " + patient.getLatitude());
                    System.out.println("Longitude: " + patient.getLongitude());
                    System.out.println("Longitude: " + patient.getLongitude());
                });

                JavaRDD<PatientRecordForProcessing> rddForCassandra = streamingContext.sparkContext().parallelize(Helper.patientList(sensorRDD));
                javaFunctions(rddForCassandra).writerBuilder("ehealth", "processed_data", mapToRow(PatientRecordForProcessing.class)).saveToCassandra();
                System.exit(1);
//
//              Dataset<Row> jdbcDF1 = sqlContext.read()
//                    .format("jdbc")
//                    .option("url", "jdbc:mysql://127.0.0.1:3306/suuber?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
//                    .option("query", String.format("select * from users where id IN%s", ids.toString()))
//                    .option("user", "root")
//                    .option("password", "").load();
//              jdbcDF1.toDF().show();
            }
//          if (!rdd.isEmpty()) {
//
//          }

//            i
        });


        streamingContext.start();              // Start the computation
        streamingContext.awaitTermination();   // Wait for the computation to terminate
    }


}