package com.ISCES.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="delegate")
public class Delegate{

    @Id
    @Column(name = "delegate_id")
    private Long delegateId;


    @OneToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;


}
