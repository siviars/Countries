package com.countries;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
public class CountryService {

    private final String nagerDate = "https://date.nager.at/api/v3/";

    public List<String> getBorders(String code) {
        if (checkCountryCode(code)) {
            return getBorderList(code);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect code");
        }
    }

    public boolean checkCountryCode(String code) {
        JsonNode jsonList = readURL(nagerDate + "AvailableCountries");
        List<JsonNode> countryCodes = jsonList.findValues("countryCode");
        return countryCodes.stream().anyMatch(cod ->
                cod.toString().replace("\"", "").equals(code.toUpperCase()));
    }

    public List<String> getBorderList(String countryCode) {
        JsonNode jsonList = readURL(nagerDate + "CountryInfo/" + countryCode.toUpperCase());
        JsonNode borders = jsonList.get("borders");
        return borders.findValues("commonName").stream().map(obj ->
                obj.toString().replace("\"", "")).toList();
    }

    public JsonNode readURL(String mapUrl) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            URL url = new URL(mapUrl);
            return mapper.readTree(url);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't read data from URL");
        }
    }
}
