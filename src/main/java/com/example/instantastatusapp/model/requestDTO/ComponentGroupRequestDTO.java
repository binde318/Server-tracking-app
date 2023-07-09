package com.example.instantastatusapp.model.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentGroupRequestDTO {
    @NotBlank
    @Length(min = 2, max = 50)
    private String name;
    private String ipAddress;
    private Integer portNumber;
    @NonNull
    private Boolean visibility;

    @NonNull
    private Boolean isEnabled;

}
