package com.example.demo.controller;

import com.example.demo.dto.JwtAuthenticationRequest;
import com.example.demo.dto.UserTokenState;
import com.example.demo.model.DriversAccount;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	
	// Prvi endpoint koji pogadja korisnik kada se loguje.
	// Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

		// Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
		// AuthenticationException

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		// Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
		// kontekst
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Kreiraj token za tog korisnika
		User user = (User) authentication.getPrincipal();
		if(!(user.getStatus().equals(Status.ACTIVE) || user.getStatus().equals(Status.UNDERREVISION))){
			return new ResponseEntity<UserTokenState>(HttpStatus.UNAUTHORIZED);
		}
		if(user.getRole().getName().equals("ROLE_DRIVER")){
			userService.changeDriverAvailabilityStatus(user.getEmail(), true);
		}
		String jwt = tokenUtils.generateToken(user.getUsername());
		int expiresIn = tokenUtils.getExpiredIn();

		// Vrati token kao odgovor na uspesnu autentifikaciju
		return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user.getRole().getName()));
	}

	@PostMapping("/logout")
	public void logout(){
		User user = userService.getLoggedIn();
		if(user.getRole().getName().equals("ROLE_DRIVER")){
			DriversAccount driver = userService.getDriverByEmail(user.getEmail());
			driver.setDriversAvailability(false);
			userService.saveDriverAvailabilityStatus(driver);
		}
	}
}
