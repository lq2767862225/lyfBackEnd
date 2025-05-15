package site.rainbowx.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


import site.rainbowx.backend.dto.auth.LoginResponse;
import site.rainbowx.backend.dto.auth.LoginUserDto;
import site.rainbowx.backend.dto.auth.RegisterUserDto;
import site.rainbowx.backend.dto.user.UserInfoDto;
import site.rainbowx.backend.entity.User;
import site.rainbowx.backend.exception.AuthorizationException;
import site.rainbowx.backend.service.AuthenticationService;
import site.rainbowx.backend.service.JwtService;
import site.rainbowx.backend.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(JwtService jwtService, AuthenticationService authenticationService, UserDetailsService userDetailsService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public UserInfoDto register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return UserInfoDto.fromUser(registeredUser);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        return new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());
    }

    @GetMapping("/me")
    public UserInfoDto getCurrentUser(@RequestHeader(value = "Authorization", defaultValue = "") String jwt_token) {
        try {
            String username = jwtService.extractUsername(jwt_token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean is_valid = jwtService.isTokenValid(jwt_token, userDetails);
            User user = userService.getUserInfo(username);
            if (is_valid) {
                return UserInfoDto.fromUser(user);
            }
        }
        catch (Exception e) {
            throw new AuthorizationException("Authorization is not valid", e);
        }
        throw new AuthorizationException("Authorization is not valid");
    }
}
