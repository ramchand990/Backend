package com.healspan.claimservice.mstupload.claim.dao.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.master.HospitalMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contact_type")
public class ContactType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="id")
    private Long id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "contact")
    private String contact;

    @Column(name = "is_active",columnDefinition = "varchar(1) default 'Y'")
    private String isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "contactType"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<HospitalMst> hospitalMst;
}
