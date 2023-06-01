package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.entity.*;
import com.example.tvDispatcher.exception.EmailAlreadyExistsException;
import com.example.tvDispatcher.exception.EmailNotFoundException;
import com.example.tvDispatcher.exception.UserBlockedException;
import com.example.tvDispatcher.exception.UserNotEnabledException;
import com.example.tvDispatcher.model.UserCreateRequest;
import com.example.tvDispatcher.model.UserUpdateRequest;
import com.example.tvDispatcher.repository.DepartmentRepository;
import com.example.tvDispatcher.repository.RoleRepository;
import com.example.tvDispatcher.repository.UserRepository;
import com.example.tvDispatcher.service.IEmailService;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final IEmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        if (user.getLocked()) {
            throw new UserBlockedException("blocked");
        }
        if (!user.getEnabled()) {
            System.out.println("not enabled");
            throw new UserNotEnabledException("notEnabled");
        }
        return user;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void updateUser(UserUpdateRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(RuntimeException::new);
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(RuntimeException::new);
        Role role = roleRepository.findByName(request.getRole());

        if (userRepository.existsByEmail(request.getEmail())) {
            if (!user.getEmail().equals(request.getEmail())) {
                throw new RuntimeException("email is already exist");
            }
        }

        user.setRole(role);
        user.setEmail(request.getEmail());
        user.setDepartment(department);

        EmployeeInfo info = user.getInfo();

        info.setName(request.getName());
        info.setSurname(request.getSurname());
        info.setMiddleName(request.getMiddleName());
        info.setGender(Gender.valueOf(request.getGender()));
        info.setPhoneNumber(request.getPhoneNumber());
        info.setAboutInformation(request.getAboutInformation());
        info.setBirthday(request.getBirthday());
        info.setAddress(request.getAddress());

        userRepository.save(user);
    }

    @Override
    public void save(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("email is already exist: " + request.getEmail());
        }
        Role role = roleRepository.findByName("USER");

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(false)
                .locked(false)
                .build();
        EmployeeInfo employeeInfo = EmployeeInfo.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .middleName(request.getMiddleName())
                .gender(Gender.valueOf(request.getGender()))
                .user(user)
                .registerDay(LocalDate.now())
                .build();
        user.setInfo(employeeInfo);

        userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        // todo тексеру
        // TODO enable false. blocked true болса қоспау
        return userRepository.findAll()
                .stream().filter(user -> {
                    return user.isEnabled() && user.isAccountNonLocked() && !user.getRole().getName().equals("ADMIN");
                })
                .toList();
    }

    @Override
    public List<User> getUsers(boolean enabled) {
        Predicate<User> userPredicate = user -> !user.getEnabled();
        if (enabled) {
            userPredicate = User::getEnabled;
        }
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getRole().getName().equals("ADMIN"))
                .filter(userPredicate)
                .toList();
    }

    @Override
    public void enableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        user.setEnabled(true);
        userRepository.save(user);
        emailService.sendMessage(user.getEmail(), "Аккаунт іске қосылды", "Qosh keldiniz! Sizdin account iske qosildy");
    }

    @Override
    public void updateLock(Long id, boolean blocked) {
        User user = userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        user.setLocked(blocked);
        userRepository.save(user);
    }

    @Override
    public void resetPasswordByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("Email not found: " + email));
        String newPassword = generateRandomPassword(6);
        String oldPassword = user.getPassword();

        user.setPassword(passwordEncoder.encode(newPassword));
        user = userRepository.save(user);

        emailService.sendMessage(email, newPassword);

        User finalUser = user;
        Runnable task = () -> {
            try {
                Thread.sleep(60000);
                String password = userRepository.findByEmail(email).get().getPassword();
                if (passwordEncoder.matches(newPassword, password)) {
                    finalUser.setPassword(oldPassword);
                    userRepository.save(finalUser);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    private String generateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        System.out.println("password: " + sb);
        return sb.toString();
    }
}
