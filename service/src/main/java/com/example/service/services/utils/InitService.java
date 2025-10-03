package com.example.service.services.utils;

import com.example.service.models.entities.RolesEntity;
import com.example.service.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class InitService {
    @Autowired
    private RoleRepository roleRepository;
    @Value("${server.port}")
    private String port;

    public void init() {
        String[] rolesname = {"USER", "ADMIN"};
        for (String name : rolesname) {
            if (roleRepository.findByName(name).isEmpty())
                roleRepository.save(RolesEntity.builder().name(name).build());
        }

        System.out.println("Port: " + port);
    }
}
