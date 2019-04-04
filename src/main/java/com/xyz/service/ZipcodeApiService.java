package com.xyz.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xyz.exceptions.BadRequestException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
    This class is used for validating the zip code against the city. When callRest is false,
    the www.zipcodeapi.com is not called. In this case Chicago's all zip codes will be provided
    in CHICAGO_ZIPCODES (type Zipcodes). The default callRest value can be changed through
    "/Contact/v1/enableZipcodeApi?flag=true"
 */
@Component
public class ZipcodeApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipcodeApiService.class);

    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${zipcodeApi.key}")
    private String zipcodeApiKey;

    private RestTemplate restTemplate;

    private final Zipcodes CHICAGO_ZIPCODES = new Zipcodes();

    private static final String URL_TEMPLATE = "https://www.zipcodeapi.com/rest/%s/city-zips.json/%s/%s";

    private static final String ERROR_1 = "No zipcodes found for %s, %s!";
    private static final String ERROR_2 = "%d does not match any zipcode in %s, %s!";

    private static final String[] ZIPCODES_IN_CHI = new String[]{"60290", "60601", "60602", "60603", "60604", "60605", "60606", "60607", "60608", "60609", "60610", "60611", "60612", "60613", "60614", "60615", "60616", "60617", "60618", "60619", "60620", "60621", "60622", "60623", "60624", "60625", "60626", "60628", "60629", "60630", "60631", "60632", "60633", "60634", "60636", "60637", "60638", "60639", "60640", "60641", "60642", "60643", "60644", "60645", "60646", "60647", "60649", "60651", "60652", "60653", "60654", "60655", "60656", "60657", "60659", "60660", "60661", "60663", "60664", "60666", "60668", "60669", "60670", "60673", "60674", "60675", "60677", "60678", "60679", "60680", "60681", "60682", "60684", "60685", "60686", "60687", "60688", "60689", "60690", "60691", "60693", "60694", "60695", "60696", "60697", "60699", "60701", "60706", "60707", "60712", "60803", "60804", "60805", "60827"};

    private boolean callRest;

    @Autowired
    public ZipcodeApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @PostConstruct
    void init() {
        restTemplate = restTemplateBuilder.
                setConnectTimeout(3000).
                setReadTimeout(3000)
                // .basicAuthorization(username, password)
                .build();

        List<String> list = Arrays.asList(ZIPCODES_IN_CHI);
        CHICAGO_ZIPCODES.setZipCodes(list);

        LOGGER.info(CHICAGO_ZIPCODES.toString());
    }

    public boolean isCallRest() {
        return callRest;
    }

    public void setCallRest(boolean callRest) {
        this.callRest = callRest;
    }

    public boolean validate(String city, String state, Integer zipcode) {
        if (StringUtils.isBlank(city) || StringUtils.isBlank(state) || zipcode == null) {
            return false;
        }

        Zipcodes zipcodesUsed = CHICAGO_ZIPCODES;
        if (callRest) {
            String url = String.format(URL_TEMPLATE, zipcodeApiKey, city, state);
            LOGGER.info("url = " + url);

            ResponseEntity<Zipcodes> resp = restTemplate.getForEntity(url, Zipcodes.class);
            zipcodesUsed = resp.getBody();

            LOGGER.info("body = \"" + zipcodesUsed + "\"");

            if (zipcodesUsed == null || zipcodesUsed.getZipCodes() == null || zipcodesUsed.getZipCodes().isEmpty()) {
                throw new BadRequestException(String.format(ERROR_1, city, state));
            }
        }

        String strZipcode = zipcode.toString();
        List<String> available = zipcodesUsed.getZipCodes();
        if (!available.contains(strZipcode)) {
            throw new BadRequestException(String.format(ERROR_2, zipcode, city, state));
        }

        return true;
    }

    // zipcodeApi Rest returns the following type
    static class Zipcodes {

        @JsonProperty("zip_codes")
        private List<String> zipCodes;

        public List<String> getZipCodes() {
            return zipCodes;
        }

        public void setZipCodes(List<String> zipCodes) {
            this.zipCodes = zipCodes;
        }

        @Override
        public String toString() {
            return "Zipcodes{" + "zipCodes=" + zipCodes + '}';
        }
    }
}
