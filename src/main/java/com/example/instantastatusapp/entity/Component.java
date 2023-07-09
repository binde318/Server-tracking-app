package com.example.instantastatusapp.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@Entity
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean isEnabled;
    private String ipAddress;
    private Integer portNumber;
    private String url;
    private Boolean visibility;
    @ManyToOne
    @JoinColumn(name="componentGroup-id")
    @JsonBackReference
    private ComponentGroup group;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="componentType-id")
    private ComponentType type;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="componentStatus-id")
    private ComponentStatus status;
//    @OneToOne(mappedBy = "component", cascade = CascadeType.ALL)
//    private Event event;

}
