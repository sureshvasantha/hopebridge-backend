package com.dns.service.impl;

import com.dns.repository.ImpactStoryRepository;
import com.dns.repository.entity.ImpactStory;
import com.dns.service.ImpactStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImpactStoryServiceImpl implements ImpactStoryService {

    private final ImpactStoryRepository storyRepository;

    @Override
    public ImpactStory createStory(ImpactStory story) {
        return storyRepository.save(story);
    }

    @Override
    public List<ImpactStory> getAllStories() {
        return storyRepository.findAll();
    }

    @Override
    public List<ImpactStory> getStoriesByCampaign(Long campaignId) {
        return storyRepository.findByCampaign_CampaignId(campaignId);
    }
}
