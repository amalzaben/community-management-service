package com.discuessit.communityManagemnet.service;

import com.discuessit.communityManagemnet.dto.controller.request.UserIdsRequest;
import com.discuessit.communityManagemnet.dto.controller.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class UserClient {

    @Autowired
    private WebClient userManagementWebClient;

    public List<UserResponse> getUsersByIds(List<Long> userIds) {
        return userManagementWebClient.post()
                .uri("/internal/users/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserIdsRequest(userIds))
                .retrieve()
                .bodyToFlux(UserResponse.class)
                .collectList()
                .block();
    }

    public UserResponse getUserById(Long userId) {
        return userManagementWebClient.get()
                .uri("/internal/users/{userId}", userId)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

}