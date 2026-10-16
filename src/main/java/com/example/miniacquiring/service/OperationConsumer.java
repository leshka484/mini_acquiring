package com.example.miniacquiring.service;

import com.example.miniacquiring.core.Const;
import com.example.miniacquiring.core.dto.CreateOperationMessage;
import com.example.miniacquiring.core.dto.CreateOperationRequest;
import com.example.miniacquiring.core.dto.OperationCreatedMessage;
import com.example.miniacquiring.core.dto.ProcessOperationMessage;
import com.example.miniacquiring.core.dto.ProcessOperationRequest;
import com.example.miniacquiring.core.enums.OperationStatus;
import com.example.miniacquiring.core.enums.OperationType;
import com.example.miniacquiring.core.exception.EntityNotFoundException;
import com.example.miniacquiring.core.exception.NotFoundException;
import com.example.miniacquiring.storage.OperationStorage;
import com.example.miniacquiring.storage.entity.OperationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationConsumer {

    private final OperationService operationService;
    private final OperationStorage operationStorage;
    private final MerchantService merchantService;

    @KafkaListener(
            topics = Const.CREATE_OPERATION_TOPIC,
            groupId = "mini-acquiring")
    @SendTo(Const.CREATE_OPERATION_REPLY_TOPIC)
    public OperationCreatedMessage create(CreateOperationMessage message) {
        try {
            log.info("Received CreateOperationMessage, merchant public key = {}", message.publicMerchantId());
            var operation = createNewOperation(message);
            var publicId = operation.getPublicId();
            log.info("Returning public id of created operation: {}", publicId);
            return new OperationCreatedMessage(publicId);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @KafkaListener(
            topics = Const.OPERATION_PAY_TOPIC,
            groupId = "mini-acquiring")
    public void pay(ProcessOperationMessage message) {
        log.info("Received PayOperationMessage, operation public id = {}", message.publicOperationId());
        var operationId = operationService.getIdByPublicId(message.publicOperationId());
        var payRequest = new ProcessOperationRequest(operationId, message.sum());
        operationService.processPayment(payRequest);
        log.info("Operation with public id = {} paid", message.publicOperationId());
    }

    @KafkaListener(
            topics = Const.OPERATION_CANCEL_TOPIC,
            groupId = "mini-acquiring")
    public void cancel(ProcessOperationMessage message) {
        var publicOperationId = message.publicOperationId();
        log.info("Received CancelOperationMessage, operation public id = {}", publicOperationId);
        var operationId = operationService.getIdByPublicId(publicOperationId);
        var cancelRequest = new ProcessOperationRequest(operationId, message.sum());
        operationService.processCancel(cancelRequest);
        log.info("Operation with public id = {} rejected", publicOperationId);
    }

    private OperationEntity createNewOperation(CreateOperationMessage message) {
        var status = operationStorage.findOperationStatus(OperationStatus.NEW);
        var type = operationStorage.findOperationType(OperationType.PAYMENT);
        var id = merchantService.getIdByPublicId(message.publicMerchantId());
        var request = new CreateOperationRequest(
                id,
                status.getId(),
                message.sum(),
                type.getId(),
                null,
                null);
        return operationService.create(request);
    }

}
