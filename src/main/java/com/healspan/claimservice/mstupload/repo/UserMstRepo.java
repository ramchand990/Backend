package com.healspan.claimservice.mstupload.repo;

import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserRoleMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserMstRepo extends JpaRepository<UserMst,Long> {
    Optional<UserMst> findByUserNameIgnoreCase(String userName);

    List<UserMst> findAllByUserRoleMst(UserRoleMst userRoleMst);
    Optional<UserMst> findByUserRoleMstId(long tpaRoleId);

}
