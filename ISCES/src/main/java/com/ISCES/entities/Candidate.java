package com.ISCES.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="candidate")
public class Candidate{

    @Id
    @Column(name = "candidate_id")
    private Long candidateId;


    @Column(name="votes")
    private Long votes;


    @ManyToOne()
    @JoinColumn(name = "student_number")
    private Student student;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "election_id")
    private Election election;

}
