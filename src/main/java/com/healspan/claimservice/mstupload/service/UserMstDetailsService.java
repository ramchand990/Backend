package com.healspan.claimservice.mstupload.service;

import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import com.healspan.claimservice.mstupload.claim.dto.master.UserMstDto;
import com.healspan.claimservice.mstupload.repo.UserMstRepo;
import com.healspan.claimservice.mstupload.security.UserMstDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserMstDetailsService implements UserDetailsService {

   private  Logger logger = Logger.getLogger(UserMstDetailsService.class.getName());
    @Autowired
    private UserMstRepo userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        logger.info("Find user record in database: " + userName);

        Optional<UserMst> user = userRepository.findByUserNameIgnoreCase(userName);

        logger.info("User found in db : " + user.isPresent());
        if (!user.isPresent())
            throw new UsernameNotFoundException("User not found: " + userName);

        return user.map(UserMstDetails::new).get();
    }

    public UserMstDto findUser(String userName){
        logger.info("Find user record for userName in database"+ userName);
        Optional<UserMst> user = userRepository.findByUserNameIgnoreCase(userName);
        logger.info("User found in db : " + user.isPresent());
        if(user.isPresent()) {
            UserMstDto userMstDto = modelMapper.map(user.get(), UserMstDto.class);
            return userMstDto;
        }
        return null;
    }
}
