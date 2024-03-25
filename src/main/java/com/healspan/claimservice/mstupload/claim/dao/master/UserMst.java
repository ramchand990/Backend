package com.healspan.claimservice.mstupload.claim.dao.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_mst")
public class UserMst {

    @Id
    @SequenceGenerator(name = "user_mst_generator", sequenceName = "user_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_mst_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserRoleMst userRoleMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private HospitalMst hospitalMst;

    @JsonIgnore
    @OneToMany(mappedBy = "userMst"
            , cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimStageLink> claimStageLinkTable;

    @JsonIgnore
    @OneToMany(mappedBy = "userMst"
            , cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimAssignment> claimAssignment;

    @Override
    public String toString() {
        return "UserMst{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", isActive=" + isActive +
                ", userRoleMst=" + userRoleMst +
                '}';
    }
}
