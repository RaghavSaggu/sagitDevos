package com.sagitDevos.sagitDevos.model.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String message;
    private Object object;
    private List<?> objectList;
    private Integer errorId;

    public static StatusDTO getInstance(){
        return new StatusDTO();
    }

    public void setErrorCodeAndMessage(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
