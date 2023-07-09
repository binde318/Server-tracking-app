package com.example.instantastatusapp.service;

import com.example.instantastatusapp.model.requestDTO.ComponentGroupRequestDTO;
import com.example.instantastatusapp.model.responseDTO.ComponentGroupResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ComponentGroupService {


     Long countGroup();

    ComponentGroupResponseDTO createComponentGroup(ComponentGroupRequestDTO componentGroupRequestDTO);

    ComponentGroupResponseDTO editComponentGroup(ComponentGroupRequestDTO componentGroupRequestDTO, Long id);

    ComponentGroupResponseDTO viewComponentGroup(Long id);

    List<ComponentGroupResponseDTO> listComponentGroup();

    void enableComponentGroup(Long id);

    void makeComponentGroupVisible(Long id);

    List<ComponentGroupResponseDTO> listVisibleComponentGroup();

}
