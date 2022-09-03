package com.zoolatech.webtask.file_management;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileManager {

    private final RequestParser parser = new RequestParser();
    private final Pattern pattern = Pattern.compile("/table/");

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

    private String getFileName(String name) {
        return pattern.matcher(name).replaceAll("");
    }

    public String convertJson(HttpServletRequest request) {
        return parser.convertJson(request);
    }
}
