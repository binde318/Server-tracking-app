package com.example.instantastatusapp.controller;

import com.example.instantastatusapp.entity.UserRole;
import com.example.instantastatusapp.model.requestDTO.UserRoleRequestDTO;
import com.example.instantastatusapp.repository.UserRoleRepository;
import com.example.instantastatusapp.service.UserRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/user/user_role")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "User Role Endpoints"
)
public class UserRoleController {

    private final UserRoleService userRoleService;
//    private final UserRoleRepository userRoleRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-role")
    public ResponseEntity<?> createRole(@RequestBody UserRoleRequestDTO userRoleRequestDTO){
        userRoleService.addRole(userRoleRequestDTO);
        return ResponseEntity.ok("New user role created");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list-user-roles")
    public ResponseEntity<List<UserRole>> fetchUserRoles() {

        List<UserRole> userRoles = userRoleService.getAllRoles();

        return ResponseEntity.ok(userRoles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/user-role/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id){
        userRoleService.deleteRole(id);
        return ResponseEntity.ok("User role deleted successfully");
    }
}
