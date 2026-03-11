package com.example.rabbitmqlab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private String orderId;
    private String customerEmail;
    private Double amount;
}
