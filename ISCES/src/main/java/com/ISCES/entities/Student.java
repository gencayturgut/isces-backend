package com.ISCES.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Table(name="student")
public class Student {

    @Id
    @Column(name="student_number")
    private Long studentNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="department_id")
    private Department department;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="grade")
    private Float grade;

    @Column(name="term")
    private Long term; // class

    @Column(name="is_voted")
    private boolean isVoted;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="email")
    private User user;

    @Column(name="is_applied")
    private Boolean isAppliedForCandidacy; // true = applied, false = didn't apply , null = rejected in this election.

    public void setIsAppliedForCandidacy(Boolean isAppliedForCandidacy){
        this.isAppliedForCandidacy = isAppliedForCandidacy;
    }


}