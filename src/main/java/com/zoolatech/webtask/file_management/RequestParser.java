package com.zoolatech.webtask.file_management;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class RequestParser {
    private String getBody(HttpServletRequest request) throws IOException {
        String stringBuilder = "";
        InputStream inputStream = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] charBuffer = new char[128];
        int bytesRead;
        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
            stringBuilder = stringBuilder.concat(String.valueOf(charBuffer, 0, bytesRead));
        }
        return stringBuilder;
    }

    public String convertJson(HttpServletRequest request) {
        String json = null;
        try {
            json = getBody(request);
            ObjectMapper mapper = new ObjectMapper();
            return (String) mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {})
                    .get("content");
        } catch (IOException e) {
            System.out.println("Can`t read body");
            throw new RuntimeException(e);
        }
    }
}
