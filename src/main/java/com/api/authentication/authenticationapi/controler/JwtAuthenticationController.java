package com.api.authentication.authenticationapi.controler;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.api.authentication.authenticationapi.model.ApplicationUser;
import com.api.authentication.authenticationapi.security.jwt.JwtRequest;
import com.api.authentication.authenticationapi.security.jwt.JwtResponse;
import com.api.authentication.authenticationapi.security.jwt.JwtTokenUtil;
import com.api.authentication.authenticationapi.service.ApplicationUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ApplicationUserService applicationUserService;

    /**
     * Authenticate a user with a given JwtRequest
     * @param authenticationRequest A JwtRequest DTO
     * @see JwtRequest
     * 
     * @return A response entiry depending on the request treatment
     * @throws Exception
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        Authentication authentication = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        UserDetails userDetails = null;
        String token = null;

        if(authentication.isAuthenticated()){
            try {
                userDetails = applicationUserService.loadUserByUsername(authenticationRequest.getUsername());
            } catch (UsernameNotFoundException e) {
                throw new UsernameNotFoundException("USERNAME NOT FOUND AFTER AUTHENTICATION");
            }
        }

        if(userDetails != null){
            token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));
        } 

        return ResponseEntity.status(409).body("Token cannot be generated");
    }


    /**
     * Register the user
     * @param user An ApplicationUser object fullfiled by the requestor
     * @see ApplicationUser
     * 
     * @return A response entiry depending on the request treatment
     * @throws Exception
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody ApplicationUser user) throws Exception {

        boolean isUserAdded = applicationUserService.addNewUser(user);

        if(!isUserAdded){
            return ResponseEntity.badRequest().body("This username is already used");
        }

        try {
            final UserDetails userDetails = applicationUserService.loadUserByUsername(user.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {

            //todo: send failure cause
            System.err.println(e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/getUser")
    public ApplicationUser getUser(Authentication authentication){
        ApplicationUser applicationUser = null;

        if(authentication != null){
            applicationUser = (ApplicationUser)authentication.getPrincipal();
        }

        return applicationUser;
    }

    /**
     * Check if the user is authenticated
     * @param request A HttpServletRequest object that contain the current request
     * @param authentication An Authentication object that contains user's authentication details
     * 
     * @return A boolean depending on the user's authentication
     */
    @GetMapping("/isAuthenticated")
    public boolean isAuthenticated(HttpServletRequest request, Authentication authentication){
        return authentication != null ? authentication.isAuthenticated() : false;
    }

    /**
     * Authenticate a user
     * @param username
     * @param password
     * @return an Authentication object
     * @throws Exception
     */
    private Authentication authenticate(String username, String password) throws Exception {

        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


}