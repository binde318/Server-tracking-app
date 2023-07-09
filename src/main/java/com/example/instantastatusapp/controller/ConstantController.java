package com.example.instantastatusapp.controller;

import com.example.instantastatusapp.entity.ComponentStatus;
import com.example.instantastatusapp.entity.ComponentType;
import com.example.instantastatusapp.model.requestDTO.StatusRequestDTO;
import com.example.instantastatusapp.model.requestDTO.TypeRequestDTO;
import com.example.instantastatusapp.service.ConstantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(
        name = "Bearer Authentication"
)
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/v1/constant")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Component Feature Endpoints"
)
public class ConstantController {
    private final ConstantService constantService;

    @PostMapping("/create-status")
    public ResponseEntity<?> createStatus(@RequestBody StatusRequestDTO statusRequestDTO){
        constantService.createStatus(statusRequestDTO);
        return ResponseEntity.ok("status created");
    }
    @PostMapping("/create-type")
    public ResponseEntity<?> createType(@RequestBody TypeRequestDTO typeRequestDTO){
        constantService.createType(typeRequestDTO);
        return ResponseEntity.ok("type created");
    }
    @DeleteMapping("delete/components-status/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable("id") Long id){
        constantService.deleteStatus(id);
        return ResponseEntity.ok("Status category deleted successfully");
    }
    @DeleteMapping("delete/component-type/{id}")
    public ResponseEntity<?> deleteType(@PathVariable("id") Long id){
        constantService.deleteType(id);
        return ResponseEntity.ok("Component type deleted successfully");
    }
    @PostMapping("edit/component-status/{id}")
    public ResponseEntity<?> editStatus(@PathVariable("id") Long id, @RequestBody StatusRequestDTO statusRequestDTO){
        ComponentStatus componentStatus= constantService.editStatus(id,statusRequestDTO);
        return ResponseEntity.ok("Status Category updated successfully");
    }
    @PostMapping("edit/component-type/{id}")
    public ResponseEntity<?> editType(@PathVariable("id") Long id, @RequestBody TypeRequestDTO typeRequestDTO){
       ComponentType componentType= constantService.editType(id,typeRequestDTO);
        return ResponseEntity.ok("Component type updated successfully");
    }

}
