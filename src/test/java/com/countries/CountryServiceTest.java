package com.countries;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {
    @Spy
    private CountryService countryServiceSpy;
    private ObjectMapper map = new ObjectMapper();
    private final String nagerDate = "https://date.nager.at/api/v3/";

    private String borders =
            "{\"commonName\":\"Latvia\",\"officialName\":\"Republic of Latvia\",\"countryCode\":\"LV\",\"region\"" +
                    ":\"Europe\",\"borders\":[{\"commonName\":\"Belarus\",\"officialName\"" +
                    ":\"Republic of Belarus\",\"countryCode\":\"BY\",\"region\":\"Europe\",\"borders\"" +
                    ":null},{\"commonName\":\"Estonia\",\"officialName\":\"Republic of Estonia\",\"countryCode\"" +
                    ":\"EE\",\"region\":\"Europe\",\"borders\":null},{\"commonName\":\"Lithuania\",\"officialName\"" +
                    ":\"Republic of Lithuania\",\"countryCode\":\"LT\",\"region\":\"Europe\",\"borders\"" +
                    ":null},{\"commonName\":\"Russia\",\"officialName\":\"Russian Federation\",\"countryCode\"" +
                    ":\"RU\",\"region\":\"Europe\",\"borders\":null}]}";

    private String countries =
            "[{\"countryCode\":\"AD\",\"name\":\"Andorra\"},{\"countryCode\":\"AL\",\"name\":\"Albania\"}," +
                    "{\"countryCode\":\"BY\",\"name\":\"Belarus\"},{\"countryCode\":\"BZ\",\"name\":\"Belize\"}," +
                    "{\"countryCode\":\"EC\",\"name\":\"Ecuador\"},{\"countryCode\":\"EE\",\"name\":\"Estonia\"}," +
                    "{\"countryCode\":\"LT\",\"name\":\"Lithuania\"},{\"countryCode\":\"LU\",\"name\":\"Luxembourg\"}," +
                    "{\"countryCode\":\"LV\",\"name\":\"Latvia\"},{\"countryCode\":\"MA\",\"name\":\"Morocco\"}," +
                    "{\"countryCode\":\"RU\",\"name\":\"Russia\"},{\"countryCode\":\"SE\",\"name\":\"Sweden\"}]";

    @BeforeEach
    void setup() {
        Mockito.when(countryServiceSpy.readURL(nagerDate + "AvailableCountries")).
                thenReturn(parseJson(countries));
    }

    @Test
    void testCorrectCountry() {
        Mockito.when(countryServiceSpy.readURL(nagerDate + "CountryInfo/LV")).
                thenReturn(parseJson(borders));
        assertEquals(Arrays.asList("Belarus", "Estonia", "Lithuania", "Russia"), countryServiceSpy.
                getBorders("LV"));
    }

    @Test
    void testFaultyCounty() {
        assertThrows(ResponseStatusException.class, () -> countryServiceSpy.getBorders("skj"));
    }

    JsonNode parseJson(String readData) {
        try {
            return map.readTree(readData);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}