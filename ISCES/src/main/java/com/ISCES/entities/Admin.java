package com.ISCES.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="admin")
public class Admin   {
    @Id
    @Column(name = "admin_id")
    private Long adminId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="email")
    private User user; // we can access password and role with this field.

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="department_id")
    private Department department; // it's null for rector.

}
