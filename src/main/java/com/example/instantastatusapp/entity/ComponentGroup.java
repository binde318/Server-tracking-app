package com.example.instantastatusapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ComponentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String ipAddress;
    private Integer portNumber;
    private Boolean visibility;
    private Boolean isEnabled;
    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER)
    @JsonManagedReference
    @ToString.Exclude
    private List<Component> component= new ArrayList<>();

}
