package com.example.instantastatusapp.service.serviceImplementation;

import com.example.instantastatusapp.entity.ComponentGroup;
import com.example.instantastatusapp.model.requestDTO.ComponentGroupRequestDTO;
import com.example.instantastatusapp.model.responseDTO.ComponentGroupResponseDTO;
import com.example.instantastatusapp.repository.ComponentGroupRepository;
import com.example.instantastatusapp.service.ComponentGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ComponentGroupsServiceImpl implements ComponentGroupService {
    private final ComponentGroupRepository componentGroupRepository;

    @Override
    public Long countGroup() {
        return componentGroupRepository.count();
    }

    @Override
    public ComponentGroupResponseDTO createComponentGroup(ComponentGroupRequestDTO componentGroupRequestDTO) {
        ComponentGroup newGroup= ComponentGroup.builder()
                .name(componentGroupRequestDTO.getName())
                .portNumber(componentGroupRequestDTO.getPortNumber())
                .ipAddress(componentGroupRequestDTO.getIpAddress())
                .isEnabled(componentGroupRequestDTO.getIsEnabled())
                .visibility(componentGroupRequestDTO.getVisibility())
                .build();
       ComponentGroup savedGroup= componentGroupRepository.save(newGroup);

                return ComponentGroupResponseDTO.builder()
                        .isEnabled(savedGroup.getIsEnabled())
                        .ipAddress(savedGroup.getIpAddress())
                        .name(savedGroup.getName())
                        .portNumber(savedGroup.getPortNumber())
                        .visibility(savedGroup.getVisibility())
                        .build();
    }

    @Override
    public ComponentGroupResponseDTO editComponentGroup(ComponentGroupRequestDTO componentGroupRequestDTO, Long id) {
        ComponentGroup componentGroup= componentGroupRepository.findById(id).orElseThrow();
        componentGroup.setName(componentGroupRequestDTO.getName());
        componentGroup.setIpAddress(componentGroup.getIpAddress());
        componentGroup.setPortNumber(componentGroupRequestDTO.getPortNumber());
        componentGroupRepository.save(componentGroup);

        return mapComponentGroupResponseDTO(componentGroup);
    }

    @Override
    public ComponentGroupResponseDTO viewComponentGroup(Long id) {
        ComponentGroup group= componentGroupRepository.findById(id).orElseThrow();
        return  mapComponentGroupResponseDTO(group);
    }

    @Override
    public List<ComponentGroupResponseDTO> listComponentGroup() {
        List<ComponentGroup> groupList= componentGroupRepository.findAll();
        List<ComponentGroupResponseDTO> listOfGroup= groupList.stream()
                .map(response->
                        mapComponentGroupResponseDTO(response)).toList();
        return listOfGroup;
    }
    private ComponentGroupResponseDTO mapComponentGroupResponseDTO(ComponentGroup componentGroup){
        return ComponentGroupResponseDTO.builder()
                .id(componentGroup.getId())
                .name(componentGroup.getName())
                .portNumber(componentGroup.getPortNumber())
                .ipAddress(componentGroup.getIpAddress())
                .visibility(componentGroup.getVisibility())
                .isEnabled(componentGroup.getIsEnabled())
                .component(componentGroup.getComponent().stream().toList())
                .numberOfComponents(componentGroup.getComponent().size())

                .build();
    }

    @Override
    public void enableComponentGroup(Long id) {
        ComponentGroup componentGroup = componentGroupRepository.findById(id).orElseThrow();
        if (componentGroup != null) {
            if (componentGroup.getIsEnabled()) {
                componentGroup.setIsEnabled(false);
            } else {
                componentGroup.setIsEnabled(true);
            }
            componentGroupRepository.save(componentGroup);

        }
    }

    @Override
    public void makeComponentGroupVisible(Long id) {
        ComponentGroup componentGroup = componentGroupRepository.findById(id).orElseThrow();
        if (componentGroup != null) {
            if (componentGroup.getVisibility()) {
                componentGroup.setVisibility(false);
            } else {
                componentGroup.setVisibility(true);
            }
            componentGroupRepository.save(componentGroup);

        }

    }

    @Override
    public List<ComponentGroupResponseDTO> listVisibleComponentGroup() {
        List<ComponentGroup> componentGroup= componentGroupRepository.findAll();

        return componentGroup.stream().filter(component ->
                        component.getVisibility().equals(true))
                .map(response ->
                        mapComponentGroupResponseDTO(response)).toList();
    }
}
