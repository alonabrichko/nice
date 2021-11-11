package com.wealthrating.rest;

import com.wealthrating.api.WealthRatingApiHelper;
import com.wealthrating.entity.RichPerson;
import com.wealthrating.request.FinancialInfo;
import com.wealthrating.request.PersonalData;
import com.wealthrating.service.RichPersonService;
import com.wealthrating.service.SendExternalRestRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class WealthRatingApi {

    @Autowired
    private RichPersonService richPersonService;

    @Autowired
    SendExternalRestRequestService sendExternalRestRequestService;

    @Autowired
    WealthRatingApiHelper wealthRatingApiHelper;

    @PostMapping("/wealth-rating/manage")
    public ResponseEntity<String> manageWealthRating(@RequestBody PersonalData personalData) {

        try {
            //1st step: Call API central-bank/regional-info/evaluate?city={city}, that will return the asset evaluation per city
            BigDecimal evaluateResponse = sendExternalRestRequestService.sendRequestToRegionalInfoEvaluationApi(personalData.getPersonalInfo().getCity());

            //2nd step: Call API central-bank/wealth-threshold, to get the threshold value to be considered as a rich
            BigDecimal threshold = sendExternalRestRequestService.sendRequestToThresholdApi();

            //3rd step: Calculate Person`s fortune
            FinancialInfo financialInfo = personalData.getFinancialInfo();
            BigDecimal fortune = financialInfo.getCash().add(new BigDecimal(financialInfo.getNumberOfAssets()).multiply(evaluateResponse));

            if (fortune.compareTo(threshold) == 1) {
                RichPerson richPerson = wealthRatingApiHelper.createRichPersonObject(personalData, fortune);
                richPersonService.saveOrUpdate(richPerson);
                return new ResponseEntity<>(String.format("Person is Rich(fortune above threshold), Record was persisted successfully. Fortune calculated: %s . Threshold: %s", fortune, threshold), HttpStatus.OK);
            } else
                return new ResponseEntity<>(String.format("Person is not Rich(fortune below threshold), no persistence performed. Fortune calculated: %s . Threshold: %s", fortune, threshold), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Exception occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
