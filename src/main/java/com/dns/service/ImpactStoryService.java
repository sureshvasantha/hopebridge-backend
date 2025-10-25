package com.dns.service;

import com.dns.repository.entity.ImpactStory;
import java.util.List;

public interface ImpactStoryService {
    ImpactStory createStory(ImpactStory story);

    List<ImpactStory> getAllStories();

    List<ImpactStory> getStoriesByCampaign(Long campaignId);
}
