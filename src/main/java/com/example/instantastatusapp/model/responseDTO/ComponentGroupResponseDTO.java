package com.example.instantastatusapp.model.responseDTO;


import com.example.instantastatusapp.entity.Component;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentGroupResponseDTO {
    private Long id;
    private String name;
    private String ipAddress;
    private Integer portNumber;
    private List<Component> component= new ArrayList<>();
    private Integer numberOfComponents;
    private Boolean visibility;
    private Boolean isEnabled;
}
