package com.ly.add.sponge.tcel.kafka;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ConsumerHandler {
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;
    Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap;

    public ConsumerHandler(String a_zookeeper, String a_groupId, String a_topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId));
        this.topic = a_topic;
        init();
    }

    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted during shutdown, exiting uncleanly");
        }
    }

    private void init() {
        Integer a_numThreads = 1;
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, a_numThreads);
        consumerMap = consumer.createMessageStreams(topicCountMap);
    }

    public List<String> take(Integer num) {
        List<String> objects = new ArrayList<>();
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        for (final KafkaStream<byte[], byte[]> stream : streams) {

            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext()) {
                objects.add(new String(it.next().message()));
                if (objects.size() >= num) {
                    return objects;
                }
            }
        }
        return objects;
    }

    public void run(int a_numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, a_numThreads);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(a_numThreads);

        // now create an object to consume the messages
        //
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new ConsumerWorker(stream, threadNumber));
            threadNumber++;
        }
    }

    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "5000");
//        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("rebalance.max.retries", "5");
        props.put("rebalance.backoff.ms", "1200");

        return new ConsumerConfig(props);
    }

    public static void main(String[] args) {
        String zooKeeper = "spmaster.bigdata.ly:2187,spslave1.bigdata.ly:2187,spslave2.bigdata.ly:2187,spslave3.bigdata.ly:2187,spslave4.bigdata.ly:2187";
        String groupId = "group2";
        String topic = "qqy_test";
        int threads = 5;

        ConsumerHandler example = new ConsumerHandler(zooKeeper, groupId, topic);
        example.run(threads);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        example.shutdown();
    }
}