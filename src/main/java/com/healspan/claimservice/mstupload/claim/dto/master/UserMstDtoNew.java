package com.healspan.claimservice.mstupload.claim.dto.master;

import com.healspan.claimservice.mstupload.claim.dao.master.HospitalMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserMstDtoNew {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String mobileNo;
    private boolean isActive;
    private String adminName;
    private String hospitalName = null;
}
