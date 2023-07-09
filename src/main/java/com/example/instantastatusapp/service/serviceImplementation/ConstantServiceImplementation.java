package com.example.instantastatusapp.service.serviceImplementation;

import com.example.instantastatusapp.entity.ComponentStatus;
import com.example.instantastatusapp.entity.ComponentType;
import com.example.instantastatusapp.exception.BadRequestException;
import com.example.instantastatusapp.model.requestDTO.StatusRequestDTO;
import com.example.instantastatusapp.model.requestDTO.TypeRequestDTO;
import com.example.instantastatusapp.repository.ComponentStatusRepository;
import com.example.instantastatusapp.repository.ComponentTypeRepository;
import com.example.instantastatusapp.service.ConstantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConstantServiceImplementation implements ConstantService {
    private final ComponentStatusRepository componentStatusRepository;
    private final ComponentTypeRepository componentTypeRepository;


    @Override
    public void createStatus(StatusRequestDTO statusRequestDTO) {
        if(componentStatusRepository.findByName(statusRequestDTO.getName()).isPresent()){
            throw new BadRequestException("Status Already Exist");
        }
        ComponentStatus newStatus= ComponentStatus.builder()
                .name(statusRequestDTO.getName())
                .build();
        componentStatusRepository.save(newStatus);
    }

    @Override
    public void createType(TypeRequestDTO typeRequestDTO) {
        if(componentTypeRepository.findByName(typeRequestDTO.getName()).isPresent()) {
            throw new BadRequestException("Type Already exist");
        }
        ComponentType newType= ComponentType.builder()
                .name(typeRequestDTO.getName())
                .build();
        componentTypeRepository.save(newType);

    }

    @Override
    public void deleteStatus(Long id) {
        componentStatusRepository.deleteById(id);
    }

    @Override
    public void deleteType(Long id) {
        componentTypeRepository.deleteById(id);
    }

    @Override
    public ComponentType editType(Long id,TypeRequestDTO typeRequestDTO) {
        ComponentType componentType= componentTypeRepository.findById(id).orElseThrow();
        componentType.setName(typeRequestDTO.getName());
        componentTypeRepository.save(componentType);
        return componentType;
    }

    @Override
    public ComponentStatus editStatus(Long id, StatusRequestDTO statusRequestDTO) {
        ComponentStatus componentStatus= componentStatusRepository.findById(id).orElseThrow();
        componentStatus.setName(statusRequestDTO.getName());
        componentStatusRepository.save(componentStatus);
      return componentStatus;
    }
}
