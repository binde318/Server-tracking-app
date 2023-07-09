package com.example.instantastatusapp.service;

import com.example.instantastatusapp.entity.ComponentStatus;
import com.example.instantastatusapp.entity.ComponentType;
import com.example.instantastatusapp.model.requestDTO.StatusRequestDTO;
import com.example.instantastatusapp.model.requestDTO.TypeRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface ConstantService {


    void createStatus(StatusRequestDTO statusRequestDTO);

    void createType(TypeRequestDTO typeRequestDTO);

    void deleteStatus(Long id);

    void deleteType(Long id);

    ComponentType editType(Long id,TypeRequestDTO typeRequestDTO);

    ComponentStatus editStatus(Long id,StatusRequestDTO statusRequestDTO);

}

