package com.wealthrating;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.wealthrating.request.FinancialInfo;
import com.wealthrating.request.PersonalData;
import com.wealthrating.request.PersonalInfo;
import com.wealthrating.service.RichPersonService;
import com.wealthrating.service.SendExternalRestRequestService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class WealthRatingApplicationTests {
    private static final String API_URL = "http://127.0.0.1:8080/wealth-rating/manage";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RichPersonService richPersonService;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private SendExternalRestRequestService sendExternalRestRequestService;

    @Test
        //Person data from the request identified as rich and was persisted
    void requestWithRichPersonReceived() throws Exception {
        Mockito.when(sendExternalRestRequestService.sendRequestToRegionalInfoEvaluationApi(Mockito.any(String.class))).thenReturn(new BigDecimal(150000));
        Mockito.when(sendExternalRestRequestService.sendRequestToThresholdApi()).thenReturn(new BigDecimal(100000));

        int id = 123456789;
        PersonalData personalData = createPersonalDataObject(id, "Tel Aviv", "Alona", "Brichko", new BigDecimal(50000000), 15);

        String mvcResponse = sendRequest(personalData);

        assertThat(mvcResponse).contains("Person is Rich(fortune above threshold), Record was persisted successfully.");
        assertThat(richPersonService.getRichPersonById(id)).isNotNull();
    }

    @Test
        //Person data from the request identified as not rich and was not persisted
    void requestWithNotRichPersonReceived() throws Exception {
        Mockito.when(sendExternalRestRequestService.sendRequestToRegionalInfoEvaluationApi(Mockito.any(String.class))).thenReturn(new BigDecimal(3600));
        Mockito.when(sendExternalRestRequestService.sendRequestToThresholdApi()).thenReturn(new BigDecimal(100000));

        int id = 111444777;
        PersonalData personalData = createPersonalDataObject(id, "Herzliya", "Mike", "Katz", new BigDecimal(1200), 3);

        String mvcResponse = sendRequest(personalData);

        assertThat(mvcResponse).contains("Person is not Rich(fortune below threshold), no persistence performed.");
        assertThat(richPersonService.getRichPersonById(id)).isNull();
    }

    @Test
        //Multiple person datas from the request received, only 2 were identified as rich and were persisted
    void multipleRequestsReceived() throws Exception {
        Mockito.when(sendExternalRestRequestService.sendRequestToRegionalInfoEvaluationApi(Mockito.any(String.class))).thenReturn(new BigDecimal(3600));
        Mockito.when(sendExternalRestRequestService.sendRequestToThresholdApi()).thenReturn(new BigDecimal(100000));

        int richPersonId1 = 147147147;
        PersonalData personalData1 = createPersonalDataObject(richPersonId1, "Arad", "Mika", "Morell", new BigDecimal(1200000000), 3);
        String mvcResponse1 = sendRequest(personalData1);

        int notRichPersonId = 258258258;
        PersonalData personalData2 = createPersonalDataObject(notRichPersonId, "Givataim", "Ran", "Atar", new BigDecimal(1200), 3);
        String mvcResponse2 = sendRequest(personalData2);

        int richPersonId2 = 369369369;
        PersonalData personalData3 = createPersonalDataObject(richPersonId2, "Holon", "Miri", "Eyni", new BigDecimal(999999900), 3);
        String mvcResponse3 = sendRequest(personalData3);


        assertThat(mvcResponse1).contains("Person is Rich(fortune above threshold), Record was persisted successfully.");
        assertThat(mvcResponse2).contains("Person is not Rich(fortune below threshold), no persistence performed.");
        assertThat(mvcResponse3).contains("Person is Rich(fortune above threshold), Record was persisted successfully.");
        assertThat(richPersonService.getAllRichPeople().size()).isEqualTo(2);
        assertThat(richPersonService.getAllRichPeople().get(0).getId()).isEqualTo(richPersonId1);
        assertThat(richPersonService.getAllRichPeople().get(1).getId()).isEqualTo(richPersonId2);
    }

    @Test
        //Exception occurred during the processing
    void exceptionOccurred() throws Exception {
        Mockito.when(sendExternalRestRequestService.sendRequestToRegionalInfoEvaluationApi(Mockito.any(String.class))).thenReturn(new BigDecimal(3600));
        Mockito.when(sendExternalRestRequestService.sendRequestToThresholdApi()).thenThrow(NullPointerException.class);

        int id = 159159159;
        PersonalData personalData = createPersonalDataObject(id, "Herzliya", "Mike", "Katz", new BigDecimal(1200), 3);

        String mvcResponse = sendRequest(personalData);

        assertThat(mvcResponse).contains("Exception occurred");
    }


    private String sendRequest(PersonalData personalData) throws Exception {
        return mockMvc.perform(post(String.format(API_URL))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personalData)))
                .andReturn().getResponse().getContentAsString();
    }


    private PersonalData createPersonalDataObject(int id, String city, String firstName, String lastName, BigDecimal cash, int numberOfAssets) {
        PersonalData personalData = new PersonalData();
        personalData.setId(id);

        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setCity(city);
        personalInfo.setFirstName(firstName);
        personalInfo.setLastName(lastName);
        personalData.setPersonalInfo(personalInfo);

        FinancialInfo financialInfo = new FinancialInfo();
        financialInfo.setCash(cash);
        financialInfo.setNumberOfAssets(numberOfAssets);
        personalData.setFinancialInfo(financialInfo);

        return personalData;
    }

}
