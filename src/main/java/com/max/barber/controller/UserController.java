package com.max.barber.controller;


import com.max.barber.model.user.User;
import com.max.barber.model.user.dtos.AuthenticationDTO;
import com.max.barber.model.user.dtos.LoginResponseDTO;
import com.max.barber.model.user.dtos.RegisterDTO;
import com.max.barber.model.user.dtos.UserInfoDTO;
import com.max.barber.repository.UserRepository;
import com.max.barber.service.TokenService;
import com.max.barber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.name(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterDTO data){
        if (this.repository.findByUsername(data.username()).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        String encrytedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.username(), data.password(), data.role());

        this.repository.save(newUser);
        String token = tokenService.generateToken(newUser);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<List<UserInfoDTO>> getAllUsers(){
        List<User> users = repository.findAll();
        List<UserInfoDTO> userInfoDTOS = users.stream()
                .map(user -> new UserInfoDTO(user.getId(), user.getUsername(),user.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userInfoDTOS);
    }
}
