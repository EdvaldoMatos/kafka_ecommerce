package br.com.alura.ecommerce;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {

    private String orderId;
    private BigDecimal amount;
    private String email;
}
