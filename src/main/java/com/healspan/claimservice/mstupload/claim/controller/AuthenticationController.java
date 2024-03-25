package com.healspan.claimservice.mstupload.claim.controller;

import com.healspan.claimservice.mstupload.claim.dto.master.UserMstDto;
import com.healspan.claimservice.mstupload.claim.dto.security.AuthenticationRequest;
import com.healspan.claimservice.mstupload.claim.dto.security.AuthenticationResponse;
import com.healspan.claimservice.mstupload.service.UserMstDetailsService;
import com.healspan.claimservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@CrossOrigin
@RequestMapping("/authentication")
class AuthenticationController {

	private static Logger logger = Logger.getLogger(AuthenticationController.class.getName());
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private UserMstDetailsService userDetailsService;

	@GetMapping("/jwt-welcome")
	public String welcomePageWithJwt(@RequestBody AuthenticationResponse authenticationResponse) {

		return "Welcome to Healspan: " + authenticationResponse.getUserName() +" your Id is: " + authenticationResponse.getUserId();
	}
	@GetMapping("/welcome")
	public String welcomePage() {

		return "Welcome to Healspan. Please authenticate yourself. Thank you.";
	}

	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		logger.info("Authentication request received for user: " + authenticationRequest.getUsername());

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			logger.info("User is not authentic: " + authenticationRequest.getUsername());
			return new ResponseEntity("Bad Credentials", HttpStatus.NOT_FOUND);

		}

		logger.info("Generating JWT token for authenticated user: " + authenticationRequest.getUsername());
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		logger.info("Successfully generated the JWT token for user: " + authenticationRequest.getUsername() + "   token   ------  " + jwt);

		 UserMstDto user = userDetailsService.findUser(userDetails.getUsername());
		if(user!=null){
			user.setJwt(jwt);
		}

		logger.info("Prepare Authentication Response for user: " + authenticationRequest.getUsername());


		//AuthenticationResponse response = new AuthenticationResponse(jwt, user.getUserName(), user.getFirstName(), user.getLastName(), user.getHospitalMst().getId(), user.getId());
		return ResponseEntity.ok(user);
	}

}

