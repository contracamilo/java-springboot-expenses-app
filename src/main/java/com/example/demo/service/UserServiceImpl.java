package com.example.demo.service;

import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.dao.DataIntegrityViolationException; // Para el delete opcional
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public UserDto registerUser(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("El email " + registerRequestDto.getEmail() + " ya está registrado.");
        }
        User user = new User();
        user.setName(registerRequestDto.getName());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        return new UserDto(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    @Override
    public AuthResponseDto loginUser(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + loginRequestDto.getEmail()));
        String token = jwtUtil.generateToken(user);
        return new AuthResponseDto(token, user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return null; 
        }
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario autenticado ("+ username +") no encontrado en la base de datos."));
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        
        user.setName(userDto.getName());
        
        if (!user.getEmail().equals(userDto.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                 throw new EmailAlreadyExistsException("El email " + userDto.getEmail() + " ya está en uso por otro usuario.");
            }
            user.setEmail(userDto.getEmail());
        }

        User updatedUser = userRepository.save(user);
        return new UserDto(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        // Opcional: Validar si tiene gastos asociados
        // if (expenseRepository.existsByUser(user)) { // Necesitaría expenseRepository y existsByUser en él
        //     throw new DataIntegrityViolationException("No se puede eliminar usuario con gastos asociados.");
        // }
        userRepository.delete(user);
    }
} 