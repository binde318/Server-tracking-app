package com.example.instantastatusapp.model.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserRoleRequestDTO {
    private String roleName;
    private String email;

    public UserRoleRequestDTO(String roleName) {
        this.roleName = roleName;
    }


}
