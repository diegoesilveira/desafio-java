package com.khipo.desafiojava.controller.v1.auth;

import com.khipo.desafiojava.controller.dto.auth.AuthenticationDto;
import com.khipo.desafiojava.controller.dto.auth.LoginResponseDto;
import com.khipo.desafiojava.controller.dto.auth.RegisterDto;
import com.khipo.desafiojava.repository.entity.UserEntity;
import com.khipo.desafiojava.service.auth.AuthorizationService;
import com.khipo.desafiojava.service.auth.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;

    private final AuthorizationService authorizationService;

    private final TokenService tokenService;

    public AuthorizationController(AuthenticationManager authenticationManager, AuthorizationService authorizationService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.authorizationService = authorizationService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto authenticationDto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDto.getLogin(), authenticationDto.getPassword());
        Authentication auth = this.authenticationManager.authenticate(authenticationToken);
        String generateToken = tokenService.generateToken((UserEntity) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(generateToken));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDto data) {
        UserDetails userDetails = authorizationService.findByLogin(data);
        return new ResponseEntity<>(userDetails, HttpStatus.CREATED);
    }
}
