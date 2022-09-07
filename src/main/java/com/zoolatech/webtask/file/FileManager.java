package com.zoolatech.webtask.file;

import jakarta.servlet.http.HttpServletRequest;

import java.io.*;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileManager {

    private final RequestParser parser = new RequestParser();
    private final Pattern pattern = Pattern.compile("/table/");

    private final String dbPath = this.getClass().getClassLoader().getResource("dbs").getPath();

    public boolean createFile(String tableName) {
        File file = new File(dbPath + tableName);

        if (file.exists()) {
            return false;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String readFile(String requestUrl) {
        StringBuilder stringBuilder = new StringBuilder();
        String fileName = getFileName(requestUrl);

        try (
                InputStream inputStream = this.getClass().getResourceAsStream("/dbs/" + fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                Stream<String> lines = bufferedReader.lines()
        ) {
            lines.forEach(s -> stringBuilder.append(s).append(" "));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    public String[] readAllFiles() {
        return new File(dbPath).list();
    }

    public boolean updateFile(String tableName, HttpServletRequest request) {
        File file = new File(dbPath + tableName);
        if (file.exists()) {
            try (FileWriter fileWriter = new FileWriter(dbPath + tableName)) {
                fileWriter.write(parser.convertJson(request));
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

    public boolean deleteFile(String tableName) {
        File file = new File(dbPath + tableName);
        return file.delete();
    }

    private String getFileName(String name) {
        return pattern.matcher(name).replaceAll("");
    }
}
