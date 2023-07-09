package com.example.instantastatusapp.controller;

import com.example.instantastatusapp.exception.BadRequestException;
import com.example.instantastatusapp.model.requestDTO.ComponentRequestDTO;
import com.example.instantastatusapp.model.responseDTO.AppResponse;
import com.example.instantastatusapp.model.responseDTO.ComponentResponseDTO;
import com.example.instantastatusapp.repository.ComponentRepository;
import com.example.instantastatusapp.service.ComponentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Component")
//@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")

@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Component Endpoints"
)
public class ComponentController {
    private final ComponentService componentService;
    private final ComponentRepository componentRepository;


    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create-component")
    public ResponseEntity<?> createComponent(@RequestBody ComponentRequestDTO componentRequestDTO) {
        if(componentRepository.findByName(componentRequestDTO.getName())!=null){
            throw new BadRequestException("Component with Name Already Exist");
        }
        if(componentRequestDTO.getPortNumber()==null) componentRequestDTO.setPortNumber(0);
        if(componentRequestDTO.getIpAddress()==null) componentRequestDTO.setIpAddress("NoIp");
        log.info("{}",componentRequestDTO.getGroup());
        ComponentResponseDTO component1 = componentService.createComponent(componentRequestDTO);
        return ResponseEntity.ok(component1);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit-component/{id}")
    public ResponseEntity<?> editComponent(@PathVariable("id") Long id, @RequestBody ComponentRequestDTO componentRequestDTO) {
        ComponentResponseDTO component = componentService.editComponent(componentRequestDTO, id);
        return ResponseEntity.ok(AppResponse.buildSuccess(component));
    }

    @GetMapping("/view-component/{id}")
    public ResponseEntity<AppResponse<ComponentResponseDTO>> viewComponent(@PathVariable Long id) {
        ComponentResponseDTO  component = componentService.viewComponent(id);
        return ResponseEntity.ok(AppResponse.buildSuccess(component));
    }

    @GetMapping("/list-component")
    public ResponseEntity<List<ComponentResponseDTO>> listComponent() {
        List<ComponentResponseDTO> component = componentService.listComponent();
        return ResponseEntity.ok(component);
    }
    @GetMapping("/list-components")
    public ResponseEntity<List<ComponentResponseDTO>> listComponents(@RequestParam("visibility")Boolean visibility) {
        List<ComponentResponseDTO> component = componentService.listComponents(visibility);
        return ResponseEntity.ok(component);
    }
    @GetMapping("/group-component")
    public ResponseEntity<?> groupComponent(@RequestBody String name){
        List<ComponentResponseDTO> groupOfComponent= componentService.getAGroupOfComponent(name);
        return ResponseEntity.ok(AppResponse.buildSuccess(groupOfComponent));
    }
    @PostMapping("/isEnabled/{id}")
    public ResponseEntity<String> enableComponent(@PathVariable("id") Long id){
       componentService.enableComponent(id);
        return ResponseEntity.ok("Enabled");
    }
    @PostMapping("/visibility/{id}")
    public ResponseEntity<?> makeComponentVisible(@PathVariable("id") Long id){
        componentService.makeComponentVisible(id);
        return ResponseEntity.ok("Enabled");
    }
    @GetMapping("/visible-component")
    public ResponseEntity<?> listVisibleComponent(){
        List<ComponentResponseDTO> componentResponseList= componentService.listVisibleComponent();
        return ResponseEntity.ok(AppResponse.buildSuccess(componentResponseList));
    }
    @DeleteMapping("/delete-component/{id}")
    public ResponseEntity<?> deleteComponent(@PathVariable("id") Long id){
        componentService.deleteComponent(id);
        return ResponseEntity.ok(AppResponse.buildSuccess("Deleted Successfully"));
    }
    @PostMapping("/assign-group/{id}")
    public ResponseEntity<?> assignGroupToComponent(@PathVariable("id") Long id,@RequestBody String groupName){
        ComponentResponseDTO component= componentService.assignGroupToComponent(id,groupName);
        return ResponseEntity.ok(component);

    }

    @GetMapping("/count-component")
    public ResponseEntity<?> countComponent(
            @RequestParam(value = "type",required = false) String type,
            @RequestParam(value = "status",required = false) String status)
    {
        Long total= componentService.countComponent(type,status);
        return ResponseEntity.ok(total);
    }
}
