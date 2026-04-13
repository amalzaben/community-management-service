package com.discuessit.communityManagemnet.dto.controller.request;


public  record CreateCommunityRequest(

        String name,

        String description,

        Boolean publicCommunity
) { }
