package com.example.instantastatusapp.model.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean enable;
    private String ipAddress;
    private Integer portNumber;
    private String url;
    private Boolean visibility;
    private String group;
    private String type;
    private String status;

}
