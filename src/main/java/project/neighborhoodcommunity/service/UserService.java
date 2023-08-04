package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.dto.UserDto;
import project.neighborhoodcommunity.exception.NotFoundException;
import project.neighborhoodcommunity.dto.RequestSignUpDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;
import project.neighborhoodcommunity.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public User join(RequestSignUpDto requestSignUpDto) {
        User user = buildUserEntity(requestSignUpDto);
        userRepository.save(user);
        return user;
    }

    private User buildUserEntity(RequestSignUpDto requestSignUpDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(requestSignUpDto, User.class);
    }

    // --------------- 회원가입 ---------------

    public String verifyToken(String token) {
        String kakaoId = jwtTokenProvider.extractIDs(token);
        System.out.println(kakaoId);
        if (!userRepository.existsByKakaoidAndRefreshToken(kakaoId, token))
            throw new NotFoundException(CommonResponseStatus.NOT_FOUND_JWT);
        return kakaoId;
    }

    // ------------ 토큰 검증 ---------------

    public UserDto getUserInfo(String kakaoId) {
        User user = findBykakaoId(kakaoId);
        return new UserDto(user);
    }

    // ------------- 회원 정보 가져오기 -------------

    public void updateInfo(String kakaoId, UserDto userDto) {
        User user = findBykakaoId(kakaoId);
        user.update(userDto);
        userRepository.save(user);
    }

    // ------------- 회원 정보 업데이트 ------------

    protected User findBykakaoId(String kakaoId) {
        return userRepository.findByKakaoid(kakaoId)
                .orElseThrow(() -> new NotFoundException(CommonResponseStatus.NOT_FOUND_USER));
    }
}
