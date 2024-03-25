package com.healspan.claimservice.mstupload.claim.dto.business;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimInfo;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dao.master.ClaimStageMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClaimAssignmentDto {
    private Long id;

    private String actionTaken;

    private Long assignedUserId;

    private UserMst assignedUser;

    private LocalDateTime assignedDateTime;

    private String assigneeComments;

    private LocalDateTime completedDateTime;
    private Long claimStageLinkId;
    private Long userMstId;
    private Long claimInfoId;
    private Long claimStageMstId;
    private List<AssignCommentDto> assignCommentDto;
}
