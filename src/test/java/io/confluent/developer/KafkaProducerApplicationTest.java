package io.confluent.developer;

import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class KafkaProducerApplicationTest {

    private final static String TEST_CONFIG_FILE = "configuration/test.properties";
    
    @Test
    public void testProduce() {
        final StringSerializer stringSerializer = new StringSerializer();
        final MockProducer<String, String> mockProducer =
                new MockProducer<>(true, stringSerializer, stringSerializer);
        final Properties props = KafkaProducerApplication.loadProperties((TEST_CONFIG_FILE));
        final String topic = props.getProperty("output.topic.name");
        final KafkaProducerApplication producerApp = new KafkaProducerApplication(mockProducer, topic);
        final List<String> records = Arrays.asList("foo-bar", "bar-foo", "baz-bar", "great:weather");

        records.forEach(producerApp::produce);

        final List<KeyValue<String, String>> expectedList = Arrays.asList(KeyValue.pair("foo", "bar"),
                KeyValue.pair("bar", "foo"),
                KeyValue.pair("baz", "bar"),
                KeyValue.pair(null, "great:weather"));

        final List<KeyValue<String, String>> actualList = mockProducer.history().stream()
                .map(this::toKeyValue).collect(Collectors.toList());

        assertThat(actualList, equalTo(expectedList));
        producerApp.shutdown();
    }

    private KeyValue<String, String> toKeyValue(final ProducerRecord<String, String> producerRecord) {
        return KeyValue.pair(producerRecord.key(), producerRecord.value());
    }
}
