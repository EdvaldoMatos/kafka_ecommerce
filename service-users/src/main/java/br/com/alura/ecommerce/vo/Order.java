package br.com.alura.ecommerce.vo;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {

    private String userId;
    private String orderId;
    private BigDecimal amount;
    private String email;
}
