package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final JSONArray jsonArray;


    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            this.jsonArray = jsonArray;


        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // This thing gets all available langues for a country
        // Read thing from jsonArray and get the languages
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("alpha3").equalsIgnoreCase(country)) {
                List<String> languageList = new ArrayList<>();
                var countryObj = jsonArray.getJSONObject(i);
                
                for (String key : countryObj.keySet()) {
                    if (!key.equals("id") && !key.equals("alpha2") && !key.equals("alpha3") && !key.equals("languages")) {
                        languageList.add(key);
                    }
                }
                return languageList;
            }
        }

        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        // This method returns all available countries as alpha3 codes
        List<String> countryList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            // Get the alpha3 code for each country and add it to the list
            String alpha3Code = jsonArray.getJSONObject(i).getString("alpha3");
            countryList.add(alpha3Code);
        }
        return countryList;
    }

    @Override
    public String translate(String country, String language) {
        // Return name of country based on the specified country abbreviation and language abbreviation.
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("alpha3").equalsIgnoreCase(country)) {
                return jsonArray.getJSONObject(i).getString(language);
            }
        }
        return null;
    }
}
