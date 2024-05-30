package com.example.shopbe.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;

    @Column(name = "provider_id")
    private Long providerId;

    private String email;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
