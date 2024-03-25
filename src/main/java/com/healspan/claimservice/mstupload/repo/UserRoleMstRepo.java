package com.healspan.claimservice.mstupload.repo;

import com.healspan.claimservice.mstupload.claim.dao.master.UserRoleMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMstRepo extends JpaRepository<UserRoleMst,Long> {
    UserRoleMst findByName(String name);
}
