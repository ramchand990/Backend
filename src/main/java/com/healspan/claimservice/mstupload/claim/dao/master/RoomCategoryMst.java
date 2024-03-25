package com.healspan.claimservice.mstupload.claim.dao.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "room_category_mst")
public class RoomCategoryMst {

    @Id
    @SequenceGenerator(name="room_category_mst_generator", sequenceName = "room_category_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_category_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    private TpaMst tpaMst;

    @Override
    public String toString() {
        return "RoomCategoryMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
