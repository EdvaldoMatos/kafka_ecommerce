package br.com.alura.ecommerce.producers;

import br.com.alura.ecommerce.configs.KafkaDispatcher;
import br.com.alura.ecommerce.vo.Email;
import br.com.alura.ecommerce.vo.Order;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
public class NewOrderMain{
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try(var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<Email>()) {
                for (var i = 0; i < 10; i++) {

                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = BigDecimal.valueOf(Math.random() * 5000 + 1);
                    var order = new Order(userId, orderId, amount);
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

                    var email = new Email("Teste","Thank you for your order! We are processing your order!");
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);

                }
                log.info("Mensagens enviada com sucesso");
            }
        }

    }
}