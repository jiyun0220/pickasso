package com.dgsw.pickasso.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private String message;
    private boolean success;
    private Long id;
    
    public ResponseDTO(String message) {
        this.message = message;
        this.success = true;
    }
    
    public ResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public ResponseDTO(boolean success, String message, Long id) {
        this.success = success;
        this.message = message;
        this.id = id;
    }
}
