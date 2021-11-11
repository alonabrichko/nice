package com.wealthrating.api;

import com.wealthrating.entity.RichPerson;
import com.wealthrating.request.PersonalData;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class WealthRatingApiHelper {

    public RichPerson createRichPersonObject(PersonalData personalData, BigDecimal fortune) {
        RichPerson richPerson = new RichPerson();
        richPerson.setId(personalData.getId());
        richPerson.setFirstName(personalData.getPersonalInfo().getFirstName());
        richPerson.setLastName(personalData.getPersonalInfo().getLastName());
        richPerson.setFortune(fortune.toString());
        return richPerson;
    }

}
