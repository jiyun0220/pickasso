package com.dgsw.pickasso.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDTO<T> {
    private String message;
    private boolean success;
    private T data;
    
    public ResponseDTO(String message) {
        this.message = message;
        this.success = true;
    }
    
    public ResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public ResponseDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
