package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Role;
import com.diego.mi_primer_api.entities.User;
import com.diego.mi_primer_api.repositories.RoleRepository;
import com.diego.mi_primer_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {

        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();

        optionalRoleUser.ifPresent(roles::add);

        if(user.isAdmin()){
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);

        if(user.isAdmin() && user.getClient() == null){
            //Registro de administrador sin ficha de cliente permitido.
        } else if(user.getClient() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente requerido para usuario no admin");
        }

        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

}
