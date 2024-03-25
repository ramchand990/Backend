package com.healspan.claimservice.mstupload.claim.dto.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AssignCommentDto {
    private Long claimStageMstId;
    private String Comments;
    private String assignedDateTime;
}