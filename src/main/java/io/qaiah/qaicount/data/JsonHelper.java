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
                    mapper.writeValue(file, Counter.createDefault());
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

    public static Counter read() {
        //if the file doesn't exist create it and return the default data set
        if (writeIfNonExistent()) {
            return Counter.createDefault();
        } else {
            Counter data = null;
            try {
                data = mapper.readValue(file, Counter.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (data == null) {
                data = Counter.createDefault();
            }

            return data;
        }
    }

    public static void save() {
        try {
            if (Main.getCounter() == null) {
                return;
            }
            mapper.writeValue(new File("data.json"), Main.getCounter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
