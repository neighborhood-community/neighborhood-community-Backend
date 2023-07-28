package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.constant.CommonResponseStatus;
import project.neighborhoodcommunity.exception.NotFoundException;
import project.neighborhoodcommunity.dto.RequestSignUpDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.jwt.JwtTokenProvider;
import project.neighborhoodcommunity.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Optional<User> join(RequestSignUpDto requestSignUpDto) {
        User user = buildUserEntity(requestSignUpDto);
        userRepository.save(user);
        return Optional.of(user);
    }

    private User buildUserEntity(RequestSignUpDto requestSignUpDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(requestSignUpDto, User.class);
    }

    public String verifyToken(String token) {
        String kakaoId = jwtTokenProvider.extractIDs(token);

        if (!userRepository.existsByKakaoidAndRefreshToken(kakaoId, token))
            throw new NotFoundException(CommonResponseStatus.NOT_FOUND_JWT);
        return kakaoId;
    }
}
