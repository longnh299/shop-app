package com.example.shopbe.services;

import com.example.shopbe.dtos.UserDTO;
import com.example.shopbe.exceptions.DataNotFoundException;
import com.example.shopbe.exceptions.InvalidParamException;
import com.example.shopbe.exceptions.PermissionDenied;
import com.example.shopbe.models.Role;
import com.example.shopbe.models.User;
import com.example.shopbe.repositories.RoleRepository;
import com.example.shopbe.repositories.UserRepository;
import com.example.shopbe.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenUtil jwtTokenUtil;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionDenied {
        String phoneNumber = userDTO.getPhoneNumber();

        // check phone number is exist or not
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        Role existingRole = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Role is not founded!"));

        // check when user create admin account from client
        long adminRoleId = roleRepository.findByName("ADMIN").getId();

        if(existingRole.getId() == adminRoleId){ // role Admin
            throw new PermissionDenied("You can't create Admin role account!");
        }

        // convert UserDTO to User
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .password(userDTO.getPassword())
                .dob(userDTO.getDob())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();



        newUser.setRole(existingRole);

        // check is user registering by facebook or google ?
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            String password = userDTO.getPassword();

            // encrypt password
            String encodePassword = passwordEncoder.encode(password);

            newUser.setPassword(encodePassword);

        }

        return userRepository.save(newUser);

    }

    @Override
    public String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException {

        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);

        if (optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid Phone number or Password!");
        }

        User currentUser = optionalUser.get();

        // check password
        if(currentUser.getFacebookAccountId() == 0 && currentUser.getGoogleAccountId() == 0){

            if (!passwordEncoder.matches(password, currentUser.getPassword())){
                throw new BadCredentialsException("Wrong Phone number or Password!");
            }
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber, password, currentUser.getAuthorities());

        // authenticate with spring security
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return jwtTokenUtil.generateToken(currentUser); // return jwt token
    }

    @Override
    public User getUser(String name) {
        return userRepository.getUserByNameUsingQuery(name);
    }


}
