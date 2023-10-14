package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class EconomicRisksJsonParser implements EconomicRisksParser {

    private final Gson gson = new GsonBuilder().create();
    @Override
    public EconomicRisks parse(Path file) {
        try (        JsonReader reader = new JsonReader(new FileReader(file.toFile()))) {
            EconomicRisks data =  gson.fromJson(reader, EconomicRisks.class);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
