package com.example.instantastatusapp.model.requestDTO;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentRequestDTO {
    @NotBlank
    @Length(min = 2, max = 50)
    private String name;
    @NotBlank
    private String description;
    @NonNull
    private Boolean isEnabled;
    private String ipAddress;
    private Integer portNumber;
    @NotBlank
    private String url;
    @NonNull
    private Boolean visibility;
    private String group;
    @NotBlank
    private String type;
    @NotBlank
    private String status;

}
