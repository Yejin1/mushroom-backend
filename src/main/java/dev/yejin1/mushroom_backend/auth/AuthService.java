package dev.yejin1.mushroom_backend.auth;


import dev.yejin1.mushroom_backend.auth.dto.LoginRequest;
import dev.yejin1.mushroom_backend.auth.dto.LoginResponse;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import dev.yejin1.mushroom_backend.org.repository.OrgUsrRepository;
import dev.yejin1.mushroom_backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OrgUsrRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        OrgUsr user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getPwd().equals(request.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtTokenProvider.generateToken(user.getLoginId());

        return LoginResponse.builder()
                .loginId(user.getLoginId())
                .usrNm(user.getUsrNm())
                .token(token)
                .build();
    }
}
