
package com.iss.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="chats")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroqChat
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String message;
}
