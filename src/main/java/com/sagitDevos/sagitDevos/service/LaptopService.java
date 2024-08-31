package com.sagitDevos.sagitDevos.service;

import com.sagitDevos.sagitDevos.model.constants.Constants;
import com.sagitDevos.sagitDevos.model.dataObjects.LaptopDataObject;
import com.sagitDevos.sagitDevos.model.dtos.LaptopDTO;
import com.sagitDevos.sagitDevos.model.dtos.StatusDTO;
import com.sagitDevos.sagitDevos.model.enitities.Laptop;
import com.sagitDevos.sagitDevos.model.exceptions.UserDefinedSagitException;
import com.sagitDevos.sagitDevos.repository.ILaptopJpaRepo;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("laptopService")
public class LaptopService implements Constants {
    @Autowired
    private ILaptopJpaRepo laptopRepo;

    public StatusDTO createEntity(LaptopDataObject employeeDataObject) {
        StatusDTO statusDTO= new StatusDTO();
        try {
            Laptop laptop = new Laptop();
            laptop.setBrandName(employeeDataObject.getBrandName());
            laptopRepo.save(laptop);
            statusDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
        } catch (ConstraintViolationException exception) {
            exception.printStackTrace();
            statusDTO.setErrorCodeAndMessage(FAIL, exception.getConstraintViolations().stream().findAny().orElseThrow().getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }

    public LaptopDTO getAllLaptopByBrandName(String orderByName) {
        return this.getAllEntitiesByBrandName(null, orderByName);
    }

    public LaptopDTO getAllEntitiesByBrandName(String brandName, String orderByName) {
        LaptopDTO laptopDTO = new LaptopDTO();
        try {
            List<Laptop> laptopList = new ArrayList<>();
            if(StringUtils.isEmpty(brandName)) {
                laptopList = laptopRepo.findAll();
            }

            if(CollectionUtils.isEmpty(laptopList)) {
                laptopDTO.setErrorCodeAndMessage(SUCCESS, NO_RECORD);
            } else {
                List<LaptopDataObject> employeeDataObjects = new ArrayList<>();
                for (Laptop laptop : laptopList) {
                    LaptopDataObject laptopDataObject = new LaptopDataObject();
                    laptopDataObject.setLid(laptop.getLid());
                    laptopDataObject.setBrandName(laptop.getBrandName());
                    employeeDataObjects.add(laptopDataObject);
                }
                laptopDTO.setData(employeeDataObjects);
                laptopDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
            }
        } catch (UserDefinedSagitException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return laptopDTO;
    }

    public LaptopDTO getAllEntitiesByIds(String idList, String orderByName) {
        LaptopDTO employeeDTO = new LaptopDTO();
        try {
            List<Laptop> laptopList = new ArrayList<>();
            if (StringUtils.isNotEmpty(idList)){
                List<Integer> ids = Arrays.asList(StringUtils.split(idList, COMMA)).stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList());
                laptopList = StringUtils.equals(orderByName, "Y") ?
                        laptopRepo.findAllByLidInOrderByBrandName(ids) :
                        laptopRepo.findAllByLidIn(ids);
            }
            if(CollectionUtils.isEmpty(laptopList)) {
                throw new UserDefinedSagitException(NO_RECORD);
            } else {
                List<LaptopDataObject> employeeDataObjects = new ArrayList<>();
                for (Laptop employee : laptopList) {
                    LaptopDataObject laptopDataObject = new LaptopDataObject();
                    laptopDataObject.setLid(employee.getLid());
                    laptopDataObject.setBrandName(employee.getBrandName());
                    employeeDataObjects.add(laptopDataObject);
                }
                employeeDTO.setData(employeeDataObjects);
                employeeDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
            }
        } catch (UserDefinedSagitException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeDTO;
    }

    public LaptopDataObject getEntityById(Integer id) {
        LaptopDataObject employeeDataObject = new LaptopDataObject();
        try {
            Laptop employee = laptopRepo.findById(id).orElse(null);
            if(employee != null){
                employeeDataObject.setLid(employee.getLid());
                employeeDataObject.setBrandName(employee.getBrandName());

                employeeDataObject.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
            } else {
                throw new UserDefinedSagitException(NO_RECORD);
            }
        } catch (UserDefinedSagitException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            employeeDataObject.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return employeeDataObject;
    }

    public StatusDTO deleteEntityById(Integer id) {
        StatusDTO statusDTO = new StatusDTO();
        try {
            Laptop laptop = laptopRepo.findById(id).orElse(null);
            if(laptop != null) {
                laptopRepo.delete(laptop);
                statusDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
            } else {
                throw new UserDefinedSagitException(NO_RECORD);
            }
        } catch (UserDefinedSagitException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }

    public StatusDTO updateEntityById(LaptopDataObject dataObject, String ifNotPresentCreateNew) {
        StatusDTO statusDTO = new StatusDTO();
        try {
            Laptop laptop = laptopRepo.findById(dataObject.getLid()).orElse(null);
            if (laptop == null) {
                statusDTO.setErrorCodeAndMessage(FAIL, NO_RECORD);
                if(org.apache.commons.lang3.StringUtils.equals(ifNotPresentCreateNew, STRING_Y)) {
                    statusDTO = this.createEntity(dataObject);
                }
            } else {
                laptop.setBrandName(dataObject.getBrandName());
                laptopRepo.save(laptop);
                statusDTO.setErrorCodeAndMessage(SUCCESS, COMMON_SUCCESS_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusDTO.setErrorCodeAndMessage(FAIL, COMMON_FAIL_MESSAGE);
        }
        return statusDTO;
    }
}
