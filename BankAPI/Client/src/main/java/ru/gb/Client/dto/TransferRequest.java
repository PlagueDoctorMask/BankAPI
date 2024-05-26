package ru.gb.Client.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * senderAccountID - id отправителя
 * receiverAccountID - id получателя
 * amount - отправляемая сумма
 */

@Data
public class TransferRequest {

    private Long senderAccountID;

    private Long receiverAccountID;

    private BigDecimal amount;
}
