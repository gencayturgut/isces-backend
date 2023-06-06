package com.ISCES.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="department")
public class Department {

    @Id
    @Column(name="department_id")
    private Long departmentId;

    @Column(name="department_name")
    private String departmentName;


}
