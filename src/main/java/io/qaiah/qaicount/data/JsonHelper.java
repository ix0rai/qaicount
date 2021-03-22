package io.qaiah.qaicount.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.qaiah.qaicount.Main;

import java.io.File;
import java.io.IOException;

public class JsonHelper {
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private static final File file = new File("data.json");

    private static boolean writeIfNonExistent() {
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    mapper.writeValue(file, new JsonObject());
                }

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        } else {
            return false;
        }
    }

    public static JsonObject read() {
        //if the file doesn't exist create it and return the default data set
        if (writeIfNonExistent()) {
            return new JsonObject();
        } else {
            JsonObject data = null;
            try {
                data = mapper.readValue(file, JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (data == null) {
                data = new JsonObject();
            }

            return data;
        }
    }

    /**
     * save the data currently stored in {@link Main#getCounters()}
     */
    public static void save() {
        try {
            if (Main.getJsonData() == null) {
                return;
            }
            mapper.writeValue(file, Main.getJsonData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
