package com.iss.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSettings
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String key;
    private String value;
}
