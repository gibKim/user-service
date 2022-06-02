package com.example.userservice.vo;

import lombok.Data;
import org.bouncycastle.crypto.agreement.jpake.JPAKERound1Payload;

import java.util.Date;

@Data
public class ResponseOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId;
}
