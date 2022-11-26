package br.com.alura.ecommerce.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {

    private String userId;
    private String orderId;
    private BigDecimal value;
}
