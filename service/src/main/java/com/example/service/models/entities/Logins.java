package com.example.service.models.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "logins")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Logins {
    @Id
    private UUID id;
    @Column(length = 500, unique = true)
    private String refreshtoken;
    private Boolean state;
    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @PrePersist
    public void prepersist(){
        state = true;
        createdAt = new Date();
    }


}
