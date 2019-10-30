import Models.KafkaPatientRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Pattern;


public class Main {
    private static final Pattern SPACE = Pattern.compile(" ");
    public static void main(String[] args) throws InterruptedException {


        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("e-health streaming");


        // Create a StreamingContext with a 1-second batch size from a SparkConf
        JavaStreamingContext streamingContext = new JavaStreamingContext(
                sparkConf, Durations.seconds(10));
        SQLContext sqlContext = new SQLContext(streamingContext.sparkContext());

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

                //saving to the list each sensor of patient
                 ArrayList <KafkaPatientRecord> patientList = new ArrayList<>();
                JSONArray sensorDataParsed = new JSONArray(sensorRDD);
                for (int i = 0; i<sensorDataParsed.length(); i++) {
                    JSONObject patient = sensorDataParsed.getJSONObject(i);
                    JSONArray sensorValues = patient.getJSONArray("SensorValues");
                    patientList.add(new KafkaPatientRecord(patient.getInt("SensorId"),sensorValues.getDouble(0),sensorValues.getDouble(1)));
                }
               patientList.forEach(patient -> {
                   System.out.println("patientId:" + patient.getId() + ",  minValue:" + patient.getMinValue());

               });
                System.exit(1);
//                String name = sensorData.getString("name");
//                System.out.println(name);
//              StringBuilder ids = new StringBuilder("(");
//                for (int i = 0; i < rdd.collect().size(); i++) {
//                    ids.append(rdd.collect().get(i));
//                    if (i != rdd.collect().size() - 1) {
//                        ids.append(",");
//                    } else {
//                        ids.append(")");
//                    }
//                }
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
//
//
//        JavaDStream<String> lines = messages.map(ConsumerRecord::value);
//        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(SPACE.split(x)).iterator());
//        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1))
//                .reduceByKey((i1, i2) -> i1 + i2);
//        lines.print();

        streamingContext.start();              // Start the computation
        streamingContext.awaitTermination();   // Wait for the computation to terminate
    }


}