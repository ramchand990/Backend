package com.healspan.claimservice.mstupload.comparator;

import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;

import java.util.Comparator;

public class UserMstComparator implements Comparator<UserMst> {
    @Override
    public int compare(UserMst object1, UserMst object2) {
        long objectOneClaimCount = object1.getId();
        long objectTwoClaimCount = object2.getId();
        if(objectOneClaimCount > objectTwoClaimCount)
            return 1;
        else if(objectOneClaimCount < objectTwoClaimCount)
            return -1;
        else
            return 0;
    }
}