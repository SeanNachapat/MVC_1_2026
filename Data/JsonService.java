package Data;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Models.ElectionData;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonService {
    private final Gson gson;
    private final String filePath;

    public JsonService() {
        this("Data" + File.separator + "seed_data.json");
    }

    public JsonService(String filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    public ElectionData loadElectionData() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("[JsonService] File not found: " + filePath);
            return null;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, ElectionData.class);
        } catch (IOException e) {
            System.err.println("[JsonService] Error reading JSON file: " + e.getMessage());
            return null;
        }
    }

    public boolean saveElectionData(ElectionData data) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(data, writer);
            return true;
        } catch (IOException e) {
            System.err.println("[JsonService] Error saving JSON file: " + e.getMessage());
            return false;
        }
    }
}
