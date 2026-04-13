package com.discuessit.communityManagemnet.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record CreateCommunityCommand (
                @NotNull(message = "User ID is required")
                Long userId,

                @NotBlank(message = "Community name is required")
                @Size(max = 60, message = "Community name must be at most 60 characters")
                String name,

                @NotBlank(message = "Description is required")
                @Size(max = 500, message = "Description must be at most 500 characters")
                String description,

                @NotNull(message = "Public Community flag is required")
                Boolean publicCommunity
){
}
