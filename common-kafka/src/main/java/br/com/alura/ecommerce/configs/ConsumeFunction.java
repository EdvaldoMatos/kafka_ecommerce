package br.com.alura.ecommerce.configs;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumeFunction<T> {
    void consume(ConsumerRecord<String, T> record);
}
