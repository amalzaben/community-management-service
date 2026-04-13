package com.discuessit.communityManagemnet.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostCommand {
    @NotNull(message = "Member ID must not be null")
    Long memberId;

    @NotNull(message = "Community ID must not be null")
    Long communityId;

    @NotBlank(message = "Title must not be blank")
    String title;

    @NotBlank(message = "Content must not be blank")
    String content;
}
