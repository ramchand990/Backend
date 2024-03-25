package com.healspan.claimservice.mstupload.claim.dao.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "gender_mst")
public class GenderMst {

    @Id
    @SequenceGenerator(name="gender_mst_generator", sequenceName = "gender_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gender_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @Override
    public String toString() {
        return "GenderMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
