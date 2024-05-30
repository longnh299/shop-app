package com.example.shopbe.controllers;

import com.example.shopbe.dtos.UserDTO;
import com.example.shopbe.dtos.UserLoginDTO;
import com.example.shopbe.models.User;
import com.example.shopbe.responses.LoginResponse;
import com.example.shopbe.responses.RegisterResponse;
import com.example.shopbe.services.IUserService;
import com.example.shopbe.utils.LocalizationUtil;
import com.example.shopbe.utils.MessageKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private IUserService userService;

    private LocalizationUtil localizationUtil;

    @Autowired
    public UserController(IUserService userService, LocalizationUtil localizationUtil){
        this.userService = userService;
        this.localizationUtil = localizationUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDto, BindingResult bindingResult) {
        try{
            if(bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(RegisterResponse.builder()
                        .message(errorMessages.toString())
                        .build());
            }

            // check password and re-password are match?
            if(!userDto.getPassword().equals(userDto.getRetypePassword())){
                return ResponseEntity.badRequest().body(RegisterResponse.builder()
                        .message(localizationUtil.getLocalizationMessage(MessageKey.USER_REGISTER_PASSWORD_NOT_MATCH))
                        .build());
            }

            User newUser = userService.createUser(userDto);

            return ResponseEntity.ok(RegisterResponse.builder()
                    .message(localizationUtil.getLocalizationMessage(MessageKey.USER_REGISTER_SUCCESSFULLY))
                    .user(newUser)
                    .build());

        } catch (Exception e){
            return ResponseEntity.badRequest().body(RegisterResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDto, HttpServletRequest request) {
        try {

            String token = userService.login(userLoginDto.getPhoneNumber(), userLoginDto.getPassword());

            String loginResponseMessage = localizationUtil.getLocalizationMessage(MessageKey.USER_LOG_IN_SUCCESSFULLY);

            LoginResponse loginResponse = new LoginResponse(loginResponseMessage, token);

            return ResponseEntity.ok(loginResponse);
        } catch(Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            LoginResponse.builder()
                                    .message(localizationUtil.getLocalizationMessage(MessageKey.USER_LOG_IN_FAILED, e.getMessage()))
                                    .build());
        }
    }

    @GetMapping("/name/{fullname}")
    public ResponseEntity<?> getUserByName(@PathVariable("fullname") String fullname){
        return ResponseEntity.ok(userService.getUser(fullname));
    }


}
