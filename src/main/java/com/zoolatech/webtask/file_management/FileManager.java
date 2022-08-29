package com.zoolatech.webtask.file_management;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileManager {

    private final Pattern pattern = Pattern.compile("/table/");

    public String readFile(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        fileName = clearFileName(fileName);

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

    private String clearFileName(String name) {
        return pattern.matcher(name).replaceAll("");
    }

    public static String getBody(HttpServletRequest request) {
        String stringBuilder = "";
        try (InputStream inputStream = request.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            char[] charBuffer = new char[128];
            int bytesRead;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder = stringBuilder.concat(String.valueOf(charBuffer, 0, bytesRead));
            }
        } catch (IOException ex) {
            System.err.println("Cannot read body");
        }
        return stringBuilder;
    }

    public String convertJson(HttpServletRequest request) throws IOException {
        String json = getBody(request);
        ObjectMapper mapper = new ObjectMapper();
        return (String) mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                })
                .get("content");
    }
}
