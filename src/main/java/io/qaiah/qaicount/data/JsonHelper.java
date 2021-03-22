package io.qaiah.qaicount.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.qaiah.qaicount.Main;

import java.io.File;
import java.io.IOException;

public class JsonHelper {
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private static final File file = new File("data.json");

    /**
     * write empty json data to a file if it doesn't exist
     * @return whether or not the file was successfully written to
     */
    private static boolean writeIfNonExistent() {
        try {
            if (!file.exists() && file.createNewFile()) {
                mapper.writeValue(file, new JsonData());
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JsonData read() {
        //if the file doesn't exist create it and return the default data set
        if (writeIfNonExistent()) {
            return new JsonData();
        } else {
            JsonData data;
            try {
                data = mapper.readValue(file, JsonData.class);
                if (data == null) {
                    data = new JsonData();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new JsonData();
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
