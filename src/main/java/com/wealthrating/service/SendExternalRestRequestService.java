package com.wealthrating.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class SendExternalRestRequestService {

    @Value("${host.and.port}")
    private String hostAndPortPrefix;

    private RestTemplate restTemplate = new RestTemplate();

    public BigDecimal sendRequestToRegionalInfoEvaluationApi(String city) {
        String uri = hostAndPortPrefix + "/central-bank/regional-info/evaluate?city=" + city;
        return restTemplate.getForObject(uri, BigDecimal.class);
    }

    public BigDecimal sendRequestToThresholdApi() {
        String uri = hostAndPortPrefix + "/central-bank/wealth-threshold";
        return restTemplate.getForObject(uri, BigDecimal.class);
    }
}
