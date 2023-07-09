package com.example.instantastatusapp.service.serviceImplementation;

import com.example.instantastatusapp.entity.Component;
import com.example.instantastatusapp.entity.ComponentGroup;
import com.example.instantastatusapp.entity.ComponentStatus;
import com.example.instantastatusapp.entity.ComponentType;
import com.example.instantastatusapp.exception.BadRequestException;
import com.example.instantastatusapp.exception.ResourceNotFoundException;
import com.example.instantastatusapp.model.requestDTO.ComponentRequestDTO;
import com.example.instantastatusapp.model.responseDTO.ComponentResponseDTO;
import com.example.instantastatusapp.repository.ComponentGroupRepository;
import com.example.instantastatusapp.repository.ComponentRepository;
import com.example.instantastatusapp.repository.ComponentStatusRepository;
import com.example.instantastatusapp.repository.ComponentTypeRepository;
import com.example.instantastatusapp.service.ComponentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ComponentServiceImpl implements ComponentService {
    private final ComponentRepository componentRepository;
    private final ComponentGroupRepository componentGroupRepository;
    private final ComponentStatusRepository componentStatusRepository;
    private final ComponentTypeRepository componentTypeRepository;

    @Override
    public ComponentResponseDTO createComponent(ComponentRequestDTO componentRequestDTO) {

        ComponentGroup componentGroup=checkGroup(componentRequestDTO);

        log.info("Found group {}",componentGroup);

      Component newComponent= Component.builder()
              .name(componentRequestDTO.getName())
              .description(componentRequestDTO.getDescription())
              .portNumber(getPortNumber(componentRequestDTO.getPortNumber(),componentRequestDTO.getGroup()))
              .status(getStatus(componentRequestDTO.getStatus()))
              .ipAddress(getIpAddress(componentRequestDTO.getIpAddress(),componentRequestDTO.getGroup()))
              .visibility(componentRequestDTO.getVisibility())
              .url(componentRequestDTO.getUrl())
              .group(componentGroup)
              .isEnabled(componentRequestDTO.getIsEnabled())
              .type(getType(componentRequestDTO.getType()))
              .build();
        log.info("build new component");
       Component component= componentRepository.save(newComponent);
        log.info("save new component");
        log.info("c g n {}",component);
       return mapComponentToResponseDTO(component);
    }

    private ComponentGroup checkGroup(ComponentRequestDTO componentRequestDTO) {
        if(componentRequestDTO.getGroup()==null) return null;
        ComponentGroup componentGroup=componentGroupRepository.findComponentGroupByName
        (componentRequestDTO.getGroup()).orElseThrow(()->new BadRequestException("Group Doesnt Exist"));
        return componentGroup;
    }

    private String getIpAddress(String ipAddress, String groupName) {
        log.info("get ip invoked");
        ComponentGroup componentGroup= componentGroupRepository.findByName(groupName);
        if (ipAddress.equals("NoIp")){
            return ((componentGroup!=null)?componentGroup.getIpAddress():"NoPort");
        }
        else {
            log.info("{}",ipAddress);
            return ipAddress;
        }
    }
    private Integer getPortNumber(Integer portNumber, String groupName) {
        log.info("invoked getPortNumber ");
        ComponentGroup componentGroup= componentGroupRepository.findByName(groupName);
        if(portNumber.equals(0)){
            return ((componentGroup!=null)?componentGroup.getPortNumber():0);
        }
        else {
            return portNumber;
        }
    }

    private ComponentStatus getStatus(String status) {
        log.info("get Status invoked");
        ComponentStatus componentStatus=componentStatusRepository.findByName(status)
                .orElseThrow(()->new ResourceNotFoundException("Status does not exist"));
       return componentStatus;
    }

    private ComponentType getType(String type) {
        log.info("get type invoked");
        ComponentType componentType= componentTypeRepository.findByName(type)
                .orElseThrow(()->new ResourceNotFoundException("Type does not exist"));
        return componentType;
    }

    @Override
    public ComponentResponseDTO editComponent(ComponentRequestDTO componentRequestDTO, Long id) {
        Component component= componentRepository.findById(id).orElseThrow(()->
                new BadRequestException("Invalid Component"));
        component.setName(componentRequestDTO.getName());
        component.setGroup(getGroup(componentRequestDTO.getGroup()));
        component.setDescription(componentRequestDTO.getDescription());
        component.setStatus(getStatus(componentRequestDTO.getStatus()));
        component.setIpAddress(componentRequestDTO.getIpAddress());
        component.setPortNumber(componentRequestDTO.getPortNumber());
        component.setType(getType(componentRequestDTO.getType()));
        componentRepository.save(component);
        return mapComponentToResponseDTO(component);
    }

    private ComponentGroup getGroup(String group) {
        return componentGroupRepository.findComponentGroupByName(group)
                .orElseThrow(()-> new ResourceNotFoundException("Group Doesnt exist"));
    }

    public ComponentResponseDTO viewComponent(Long id) {
        Component component= componentRepository.findById(id).orElseThrow();
        ComponentResponseDTO componentResponseDTO=
       mapComponentToResponseDTO(component);
        return componentResponseDTO;
    }

    @Override
    public List<ComponentResponseDTO> listComponent() {
        List<Component> componentList= componentRepository.findAll();
        return componentList.stream()
                .map(component ->
                mapComponentToResponseDTO(component)).toList();
    }

    @Override
    public List<ComponentResponseDTO> getAGroupOfComponent(String name) {
       List<Component> list= componentRepository.findAll();
       List<ComponentResponseDTO> groupedComponent=list.stream()
//               .filter(component -> component.getGroup().getName().equals(name))
               .map(resp->mapComponentToResponseDTO(resp))
               .toList();
        return (groupedComponent);
    }

    @Override
    public void enableComponent(Long id) {
        Component component=componentRepository.findById(id).orElseThrow();
        if (component==null){
            throw new RuntimeException();
        }
            if (component.getIsEnabled()){
                component.setIsEnabled(false);
            }
            else {
                component.setIsEnabled(true);
            }
            componentRepository.save(component);
        }

    @Override
    public void makeComponentVisible(Long id) {
        Component component=componentRepository.findById(id).orElseThrow();
        if (component==null){
            throw new RuntimeException();
        }
        if (component.getVisibility()){
            component.setVisibility(false);
        }
        else {
            component.setVisibility(true);
        }
       componentRepository.save(component);
    }
    private ComponentResponseDTO mapComponentToResponseDTO(Component component){
        return ComponentResponseDTO.builder()
                .id(component.getId())
                .name(component.getName())
                .status(component.getStatus().getName())
                .ipAddress(component.getIpAddress())
                .portNumber(component.getPortNumber())
                .type(component.getType().getName())
                .enable(component.getIsEnabled())
                .url(component.getUrl())
                .description(component.getDescription())
                .visibility(component.getVisibility())
                .group((component.getGroup() != null) ? component.getGroup().getName() : null)
                .build();
    }

    @Override
    public List<ComponentResponseDTO> listVisibleComponent() {
        List<Component> components= componentRepository.findAll();

         return components.stream()
                 .filter(component ->
                    component.getVisibility().equals(true))
                 .map(this::mapComponentToResponseDTO).toList();
    }

    @Override
    public void deleteComponent(Long id) {
         componentRepository.deleteById(id);
    }

    @Override
    public ComponentResponseDTO assignGroupToComponent(Long id, String groupName) {
        Component component= componentRepository.findById(id).orElseThrow(()->
                new BadRequestException("Component doesnt exist"));
        component.setGroup(componentGroupRepository.findComponentGroupByName(groupName)
                .orElseThrow(()-> new ResourceNotFoundException("Group not found" +
                "")));
        componentRepository.save(component);
        return mapComponentToResponseDTO(component);
    }


    @Override
    public Long countComponent(String type, String status) {
        if (type==null&&status==null){
            return componentRepository.count();
        }
       return  (status==null?componentRepository.countComponentByType(type)
               :componentRepository.countComponentByStatus(status));
    }

    @Override
    public List<ComponentResponseDTO> listComponents(Boolean visibility) {
        List<Component> componentList= componentRepository.findAll();
        return componentList.stream()
                .filter(resp->resp.getVisibility()==visibility)
                .map(component ->
                        mapComponentToResponseDTO(component)).toList();
    }
}
