package br.com.alura.ecommerce.consumers;

import br.com.alura.ecommerce.configs.KafkaService;
import br.com.alura.ecommerce.vo.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.HashMap;


public class FraudDectetorService {

    public static void main(String[] args) {
        var fraudDectetorService = new FraudDectetorService();
        try (var service = new KafkaService(FraudDectetorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER", fraudDectetorService::parse, Order.class, new HashMap<>())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) {
        System.out.println("---------------------------------------- ");
        System.out.println("Processing new order, checking for fraud ");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // ignoring
            throw new RuntimeException(e);
        }
        System.out.println("Order processed whith sucess");
    }
}
