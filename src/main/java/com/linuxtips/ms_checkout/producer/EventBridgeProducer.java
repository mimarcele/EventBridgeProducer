package com.linuxtips.ms_checkout.producer;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.eventbridge.AmazonEventBridge;
import com.amazonaws.services.eventbridge.AmazonEventBridgeAsyncClient;
import com.amazonaws.services.eventbridge.model.PutEventsRequest;
import com.amazonaws.services.eventbridge.model.PutEventsRequestEntry;
import com.amazonaws.services.eventbridge.model.PutEventsResult;
import com.linuxtips.ms_checkout.model.Pagamento;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class EventBridgeProducer {

    @Bean
    private AmazonEventBridge amazonEventBridge() {
        return AmazonEventBridgeAsyncClient.builder()
                .withRegion("us-east-2")
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

    }

    public void finishOrder(Pagamento pagamento){
        PutEventsRequestEntry putEventsRequestEntry = new PutEventsRequestEntry();
        putEventsRequestEntry.withSource(pagamento.origem)
                .withDetail("{\"valor\": \"" + pagamento.valor + "\"}")
                .withDetailType(pagamento.status)
                .withEventBusName("status-pedido-bus");

        final PutEventsRequest request = new PutEventsRequest();
        request.withEntries(putEventsRequestEntry);

        final PutEventsResult result = amazonEventBridge().putEvents(request);

        Logger.getLogger("Evento enviado para a AWS " + result);
    }

}
