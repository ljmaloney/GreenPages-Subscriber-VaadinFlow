package com.green.yp.subscriber.service.reference;

import com.green.yp.api.apitype.reference.LineOfBusinessApi;
import com.green.yp.api.apitype.reference.SubscriptionApi;
import com.green.yp.subscriber.integration.reference.ReferenceFeignClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReferenceService {

    final
    ReferenceFeignClient referenceFeignClient;

    public ReferenceService(ReferenceFeignClient referenceFeignClient) {
        this.referenceFeignClient = referenceFeignClient;
    }

    public List<LineOfBusinessApi> getLinesOfBusiness() {
        return referenceFeignClient.getLineOfBusiness().getResponse();
    }

    public List<SubscriptionApi> getSubscriptions() {
        return referenceFeignClient.getSubscriptions().getResponse();
    }
}
