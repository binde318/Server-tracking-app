package com.example.instantastatusapp.service;

import com.example.instantastatusapp.model.requestDTO.ComponentRequestDTO;
import com.example.instantastatusapp.model.responseDTO.ComponentResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ComponentService {
    ComponentResponseDTO createComponent(ComponentRequestDTO componentRequestDTO);

    ComponentResponseDTO editComponent(ComponentRequestDTO componentRequestDTO, Long id);

    ComponentResponseDTO  viewComponent(Long id);

    List<ComponentResponseDTO> listComponent();

    List<ComponentResponseDTO> getAGroupOfComponent(String name);

    void enableComponent(Long id);

    void makeComponentVisible(Long id);

    List<ComponentResponseDTO> listVisibleComponent();

    void deleteComponent(Long id);

    ComponentResponseDTO assignGroupToComponent(Long id, String groupName);


    Long countComponent(String type, String status);

    List<ComponentResponseDTO> listComponents(Boolean visibility);
}
