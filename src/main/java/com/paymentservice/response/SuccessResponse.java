package com.paymentservice.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SuccessResponse {
    private List<Object> data;

    public SuccessResponse() {
        data = new ArrayList<>();
    }

    public List<Object> getData() {
        return data;
    }

    public void addData(Object data) {
        this.data.add(data);
    }

    public String convertToJson() throws JsonProcessingException {
        String response = new ObjectMapper().writeValueAsString(this);
        data = new ArrayList<>();
        return response;
    }
}
