package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name="user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String nickname;
}
