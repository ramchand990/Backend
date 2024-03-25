package com.healspan.claimservice.mstupload.claim.constants;

public class Constant {

    public static final int INIT_AUTH_PENDING_DOCUMENTS = 1;

    public enum RequestType{EDIT,CREATE}
    public enum ResponseStatus{FAILED,SUCCESS}
    public enum ClaimStage{INITIAL_AUTHORIZATION,ENHANCEMENT,DISCHARGE,FINAL_CLAIM}
    public enum ClaimStatus{
        PENDING_DOCUMENTS,
        PENDING_HS_APPROVAL,
        PENDING_TPA_APPROVAL,
        TPA_QUERY,
        APPROVED,
        REJECTED,
        HARD_COPIES_TO_BE_SENT,
        DOCUMENTS_DISPATCHED,
        SETTLED,
        QUERIED
    }

    public enum AssignmentFlowType{
        HOSPITAL_USER_SUBMITTED_CLAIM,
        HEALSPAN_USER_APPROVED_CLAIM,
        HEALSPAN_USER_QUERIED_AGAINST_CLAIM,
        TPA_USER_APPROVED_CLAIM,
        TPA_USER_QUERIED_AGAINST_CLAIM,
        TPA_USER_REJECTED_CLAIM
    }
    public enum UserRole{ADMIN,HOSPITAL,HEALSPAN,TPA,COURIER}
}
