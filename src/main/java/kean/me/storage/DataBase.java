package kean.me.storage;

import kean.me.Main;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataBase {
    private final File folder;

    public DataBase() {
        try {
            File jarLocation = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            System.out.println("[Data Base]: JAR location = "+jarLocation.getPath());
            folder = new File(jarLocation, "../GameBot/Users");
            folder.mkdirs();
            System.out.println("[Data Base]: Data folder location = "+folder.getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String filename, JSONObject object) {
        try {
            File file = new File(folder, filename);
            if (!file.exists()) file.createNewFile();
            FileWriter writer = new FileWriter(file);
            object.write(writer,4,0);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public JSONObject read(String filename){
        try {
            File file = new File(folder, filename);
            if (!file.exists()) return new JSONObject();
            return new JSONObject(new String(new FileInputStream(file).readAllBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public JSONObject[] getAllUserData(){
        File[] files = folder.listFiles();
        if (files == null) return new JSONObject[]{new JSONObject()};
        return Arrays.stream(files).map(file -> {
            try {
                return new JSONObject(new String(new FileInputStream(file).readAllBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toArray(JSONObject[]::new);
    }
}
