package com.example.instantastatusapp.controller;

import com.example.instantastatusapp.exception.BadRequestException;
import com.example.instantastatusapp.model.requestDTO.ComponentGroupRequestDTO;
import com.example.instantastatusapp.model.responseDTO.AppResponse;
import com.example.instantastatusapp.model.responseDTO.ComponentGroupResponseDTO;
import com.example.instantastatusapp.repository.ComponentGroupRepository;
import com.example.instantastatusapp.service.ComponentGroupService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Group")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Component Group Endpoints"
)
public class ComponentGroupController {
    private final ComponentGroupService componentGroupService;
    private final ComponentGroupRepository componentGroupRepository;


    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create-componentGroup")
    public ResponseEntity<?> createComponent(@RequestBody ComponentGroupRequestDTO componentGroupRequestDTO) {
        if(componentGroupRepository.findByName(componentGroupRequestDTO.getName())!=null){
            throw new BadRequestException("Group with Name Already Exist");
        }
        ComponentGroupResponseDTO componentGroup = componentGroupService
                .createComponentGroup(componentGroupRequestDTO);
        return ResponseEntity.ok(AppResponse.buildSuccess(componentGroup));
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/editComponentGroup/{id}")
    public ResponseEntity<?> editComponentGroup(@PathVariable("id") Long id,
                                                @RequestBody ComponentGroupRequestDTO componentGroupRequestDTO) {
        ComponentGroupResponseDTO componentGroup = componentGroupService.editComponentGroup(componentGroupRequestDTO, id);
        return ResponseEntity.ok(AppResponse.buildSuccess(componentGroup));
    }

    @GetMapping("/viewGroup/{id}")
    public ResponseEntity<ComponentGroupResponseDTO> viewGroup(@PathVariable("id") Long id) {
        ComponentGroupResponseDTO componentGroup = componentGroupService.viewComponentGroup(id);
        return ResponseEntity.ok(componentGroup);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/listGroup")
    public ResponseEntity<List<ComponentGroupResponseDTO>> listGroup() {
        List<ComponentGroupResponseDTO> componentGroup = componentGroupService.listComponentGroup();
        return ResponseEntity.ok(componentGroup);
    }
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/countGroup")
    public ResponseEntity<?> countGroup(){
      Long numberOfGroups= componentGroupService.countGroup();
        return ResponseEntity.ok(AppResponse.buildSuccess((numberOfGroups)));
    }
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/isEnabled/{id}")
    public ResponseEntity<String> enableComponentGroup(@PathVariable("id") Long id){
        componentGroupService.enableComponentGroup(id);
        return ResponseEntity.ok("Enabled");
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/visibility/{id}")
    public ResponseEntity<?> makeComponentGroupVisible(@PathVariable("id") Long id){
        componentGroupService.makeComponentGroupVisible(id);
        return ResponseEntity.ok(AppResponse.buildSuccess("Visible"));
    }
    @GetMapping("/visible-componentGroup")
    public ResponseEntity<?> listVisibleComponentGroup(){
        List<ComponentGroupResponseDTO> componentResponseList= componentGroupService.listVisibleComponentGroup();
        return ResponseEntity.ok(AppResponse.buildSuccess(componentResponseList));
    }
}
