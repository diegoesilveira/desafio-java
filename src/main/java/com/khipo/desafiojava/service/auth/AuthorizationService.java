package com.khipo.desafiojava.service.auth;

import com.khipo.desafiojava.controller.dto.auth.RegisterDto;
import com.khipo.desafiojava.exception.UsuarioNaoEncontradoException;
import com.khipo.desafiojava.repository.UserRepository;
import com.khipo.desafiojava.repository.entity.UserEntity;
import com.khipo.desafiojava.util.AuthEncrypt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.khipo.desafiojava.util.MessagemException.USUARIO_NAO_ENCONTRADO;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO));
        return userDetails;

    }

    public UserDetails findByLogin(RegisterDto registerDto) {
        String encodedPassword = AuthEncrypt.encodePassword(registerDto.password());
        return userRepository.findByLogin(registerDto.login())
                .orElse(userRepository.save(new UserEntity(registerDto.login(), encodedPassword, registerDto.role())));
    }

}
