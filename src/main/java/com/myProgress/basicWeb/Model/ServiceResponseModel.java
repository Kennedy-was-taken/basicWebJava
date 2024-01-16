package com.myProgress.basicWeb.Model;

import lombok.Data;

@Data
public class ServiceResponseModel <T> {
    
    private T Data;
    private boolean isSuccessful;
    private String Message;
}
