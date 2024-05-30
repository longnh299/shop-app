package com.example.shopbe.responses;

import com.example.shopbe.models.User;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {

    private String message;

    private User user;

}
