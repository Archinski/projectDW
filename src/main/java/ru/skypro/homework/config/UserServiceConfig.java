package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с пользователями, реализующий интерфейс UserDetailsService.
 * Используется для аутентификации пользователей в Spring Security.
 */
@Service
@RequiredArgsConstructor
public class UserServiceConfig implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Метод поиска пользователя в таблице пользователя по имени пользователя.
     *
     * @param username имя пользователя.
     * @return объект Optional, содержащий пользователя.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Метод аутентификации пользователя в Spring Security.
     * Ищет пользователя в базе данных с помощью метода {@link UserServiceConfig#findByUsername(String)}
     * и преобразует его в объект UserDetails.
     *
     * @param username имя пользователя.
     * @return объект класса UserDetails.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
