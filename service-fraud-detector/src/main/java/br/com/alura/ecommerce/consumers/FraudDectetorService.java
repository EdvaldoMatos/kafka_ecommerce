package br.com.alura.ecommerce.consumers;

import br.com.alura.ecommerce.configs.KafkaDispatcher;
import br.com.alura.ecommerce.configs.KafkaService;
import br.com.alura.ecommerce.vo.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class FraudDectetorService {

    public static void main(String[] args) {
        var fraudDectetorService = new FraudDectetorService();
        try (var service = new KafkaService(FraudDectetorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER", fraudDectetorService::parse, Order.class, new HashMap<>())) {
            service.run();
        }
    }

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();


    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
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
        var order = record.value();
        if (isFraud(order)) {
            System.out.println("Order is a fraud!!!");
            orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getUserId(), order );

        } else {
            System.out.println("Approved: " + order);
            orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getUserId(), order );
        }
        System.out.println("sed whith suceOrder processs");
    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;

    }
}
