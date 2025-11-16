package com.linuxtips.ms_checkout.controller;

import com.linuxtips.ms_checkout.model.Pagamento;
import com.linuxtips.ms_checkout.producer.EventBridgeProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {

    private EventBridgeProducer eventBridgeProducer;

    public CheckoutController(EventBridgeProducer eventBridgeProducer) {
        this.eventBridgeProducer = eventBridgeProducer;
    }

    @PostMapping("/orders")
    public void finishOrder(@RequestBody Pagamento pagamento){
        eventBridgeProducer.finishOrder(pagamento);
    }
}
