package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername()); // ✅ username으로 수정
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // 실제론 암호화해야 함
        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(request.getPassword())) {
                Map<String, String> result = new HashMap<>();
                result.put("username", user.getUsername());
                result.put("email", user.getEmail());
                return ResponseEntity.ok("로그인 성공");
            } else {
                return ResponseEntity.status(401).body("비밀번호가 일치하지 않습니다.");
            }
        } else {
            return ResponseEntity.status(404).body("해당 이메일의 사용자가 존재하지 않습니다.");
        }
    }
}
