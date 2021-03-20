package io.qaiah.qaicount.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.qaiah.qaicount.Main;
import io.qaiah.qaicount.data.CountingData;

import java.io.File;
import java.io.IOException;

public class JsonHelper {
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private static final File file = new File("data.json");

    private static boolean writeIfNonExistent() {
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    mapper.writeValue(file, CountingData.createDefault());
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

    public static CountingData read() {
        //if the file doesn't exist create it and return the default data set
        if (writeIfNonExistent()) {
            return CountingData.createDefault();
        } else {
            CountingData data = null;
            try {
                data = mapper.readValue(file, CountingData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (data == null) {
                data = CountingData.createDefault();
            }

            return data;
        }
    }

    public static void save() {
        try {
            if (Main.getData() == null) {
                return;
            }
            mapper.writeValue(new File("data.json"), Main.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
