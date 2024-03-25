package com.healspan.claimservice.mstupload.claim.dao.business;

import com.healspan.claimservice.mstupload.claim.dao.master.ClaimStageMst;
import com.healspan.claimservice.mstupload.claim.dao.master.StatusMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserRoleMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "claim_assignment")
public class ClaimAssignment {

    @Id
    @SequenceGenerator(name="claim_assignment_generator", sequenceName = "claim_assignment_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "claim_assignment_generator")
    @Column(name="id")
    private Long id;

    @Column(name="action_taken")
    private String actionTaken;

    @Column(name="assigned_comments")
    private String assigneeComments;

    @Column(name="assigned_date_time")
    private LocalDateTime assignedDateTime;

    @Column(name="completed_date_time")
    private LocalDateTime completedDateTime;

    @Column(name="iteration_instance")
    private long iterationInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserMst userMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserRoleMst assignedToUserRoleMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimStageLink claimStageLink;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimInfo claimInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimStageMst claimStageMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private StatusMst statusMst;

    @Override
    public String toString() {
        return "ClaimAssignment{" +
                "id=" + id +
                ", actionTaken='" + actionTaken + '\'' +
                ", assignedTo=" + userMst.getUserName() +
                ", assignedDateTime=" + assignedDateTime +
                ", assigneeComments='" + assigneeComments + '\'' +
                ", completedDateTime=" + completedDateTime +
                '}';
    }

}
