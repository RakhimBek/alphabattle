package ru.raimbek.rakhimbekov.alphabattle.helpers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.http.converter.json.GsonBuilderUtils;
import ru.raimbek.rakhimbekov.alphabattle.models.Payment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ExternalPaymentsHelper {

    public static List<Payment> getPayments() {
        // kafka-console-consumer --bootstrap-server localhost:9092 --topic RAW_PAYMENTS --from-beginning
        final Properties props = new Properties();
        props.put("bootstrap.servers", "178.154.247.10:29092");
        props.put("group.id", "test01");
        props.put("auto.offset.reset", "earliest");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        final String topicName = "RAW_PAYMENTS";
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(topicName));

        final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
        final List<Payment> payments = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            final Payment payment = GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays()
                    .create()
                    .fromJson(record.value(), Payment.class);

            payments.add(payment);
        }

        return payments;
    }

    public static List<Payment> getPaymentsFromFile() {
        final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("data.txt");
        if (resourceAsStream == null) {
            return Collections.emptyList();
        }

        final List<Payment> payments = new ArrayList<>();
        final File file = new File("data.txt");
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    payments.add(GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays()
                            .create()
                            .fromJson(line, Payment.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return payments;
    }

    public static void main(String[] args) {
        ExternalPaymentsHelper.getPayments();

    }
}
