import Helpers.Helper;
import Helpers.JsonParser;
import Models.CassandraModel;
import Models.KafkaPatientRecord;
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

import java.util.*;
import java.util.regex.Pattern;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

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
                List<CassandraModel> cassandraModel = new ArrayList<>();
                for (int i = 0; i < JsonParser.ParseKafkaData(sensorRDD).size(); i ++) {
                    KafkaPatientRecord kafkaData = JsonParser.ParseKafkaData(sensorRDD).get(i);
                    PatientRecordForProcessing patientDatabase = Helper.patientList(sensorRDD).get(i);

                    if (patientDatabase.getParameterUnit().equals("mmHg") || patientDatabase.getParameterUnit().equals("minute")) {
                        if (kafkaData.getMinValue() >= patientDatabase.getParameterNormalMinValue() && kafkaData.getMaxValue() <= patientDatabase.getParameterNormalMaxValue()) {
                             patientDatabase.setStatus("normal");
                        }
                         if (kafkaData.getMaxValue() > (patientDatabase.getParameterMaxValue() - 5)){
                            patientDatabase.setStatus("critical high");
                        }
                         if (kafkaData.getMinValue() < (patientDatabase.getParameterMinValue() + 5)){
                            patientDatabase.setStatus("critical low");

                        }
                         if (kafkaData.getMaxValue() > (patientDatabase.getParameterNormalMaxValue() + 5) && kafkaData.getMaxValue() <= patientDatabase.getParameterMaxValue() -5) {
                            patientDatabase.setStatus("high");
                        }
                         if (kafkaData.getMinValue() < (patientDatabase.getParameterNormalMinValue() -5) && kafkaData.getMinValue() >= patientDatabase.getParameterMinValue() +5) {
                            patientDatabase.setStatus("low");
                        }
                    } else {
                        if (kafkaData.getMinValue() >= patientDatabase.getParameterNormalMinValue() || kafkaData.getMinValue() <= patientDatabase.getParameterNormalMaxValue()){
                            patientDatabase.setStatus("normal");
                        }else if (kafkaData.getMinValue() <= (patientDatabase.getParameterMinValue() + 0.5)) {
                            patientDatabase.setStatus("critical low");
                        } else if (kafkaData.getMinValue() >= (patientDatabase.getParameterNormalMinValue() + 0.5) && kafkaData.getMinValue() < patientDatabase.getParameterNormalMinValue()) {
                            patientDatabase.setStatus("low");
                        }else if (kafkaData.getMinValue() <= (patientDatabase.getParameterMinValue() + 2) && kafkaData.getMinValue() > patientDatabase.getParameterMaxValue()) {
                            patientDatabase.setStatus("high");
                        }
                    }


                    cassandraModel.add(
                        new CassandraModel(
                                UUID.randomUUID(),
                                patientDatabase.getDoctorId(),
                                patientDatabase.getPatientId(),
                                patientDatabase.getSensorId(),
                                patientDatabase.getVisitId(),
                                patientDatabase.getLatitude(),
                                patientDatabase.getLongitude(),
                                patientDatabase.getParameterMinValue(),
                                patientDatabase.getParameterMaxValue(),
                                patientDatabase.getParameterNormalMaxValue(),
                                patientDatabase.getParameterNormalMinValue(),
                                patientDatabase.getParameterUnit(),
                                patientDatabase.getStatus(),
                                kafkaData.getMinValue(),
                                kafkaData.getMaxValue()
                        )
                    );

                }
//                     making a model for cassandra
                  JavaRDD<CassandraModel> patientRDD = streamingContext.sparkContext().parallelize(cassandraModel);
                  javaFunctions(patientRDD).writerBuilder("ehealth", "processed_data", mapToRow(CassandraModel.class)).saveToCassandra();
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