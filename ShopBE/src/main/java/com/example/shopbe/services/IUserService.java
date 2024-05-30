package com.example.shopbe.services;

import com.example.shopbe.dtos.UserDTO;
import com.example.shopbe.exceptions.DataNotFoundException;
import com.example.shopbe.exceptions.InvalidParamException;
import com.example.shopbe.exceptions.PermissionDenied;
import com.example.shopbe.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionDenied;

    String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException;

    User getUser(String name);
}
