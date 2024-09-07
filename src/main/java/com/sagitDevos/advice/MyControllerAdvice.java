package com.sagitDevos.advice;

import com.sagitDevos.model.constants.Constants;
import com.sagitDevos.model.dtos.StatusDTO;
import com.sagitDevos.model.exceptions.InvalidParameterSagitException;
import com.sagitDevos.model.exceptions.NullPointerSagitException;
import com.sagitDevos.model.exceptions.UserDefinedSagitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice implements Constants {

    @ExceptionHandler(UserDefinedSagitException.class)
    public ResponseEntity<StatusDTO> handlerSagitException (UserDefinedSagitException exception) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCodeAndMessage(FAIL, "Some problem with request");
        return new ResponseEntity<StatusDTO>(statusDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerSagitException.class)
    public ResponseEntity<StatusDTO> handlerGenericException (NullPointerSagitException exception) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCodeAndMessage(FAIL, "Null value found");
        return new ResponseEntity<StatusDTO>(statusDTO, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidParameterSagitException.class)
    public ResponseEntity<StatusDTO> handlerInvalidParamException (InvalidParameterSagitException exception) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCodeAndMessage(FAIL, "Data passes is not valid");
        return new ResponseEntity<StatusDTO>(statusDTO, HttpStatus.BAD_REQUEST);
    }
}
