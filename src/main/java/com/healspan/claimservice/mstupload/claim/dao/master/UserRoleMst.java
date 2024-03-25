package com.healspan.claimservice.mstupload.claim.dao.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_role_mst")
public class UserRoleMst {

    @Id
    @SequenceGenerator(name="user_role_mst_generator", sequenceName = "user_role_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active",columnDefinition = "varchar(1) default 'Y'")
    private String isActive;
    @JsonIgnore
    @OneToMany(mappedBy = "userRoleMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<UserMst> userMst;

    @JsonIgnore
    @OneToMany(mappedBy = "userRoleMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<StatusMst> statusMst;

    @JsonIgnore
    @OneToMany(mappedBy = "assignedToUserRoleMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimAssignment> claimAssignment;

    @Override
    public String toString() {
        return "UserRoleMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
