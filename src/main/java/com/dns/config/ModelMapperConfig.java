package com.dns.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dns.dto.CampaignDTO;
import com.dns.repository.entity.Campaign;

@Configuration
public class ModelMapperConfig {
    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // Custom rule: map Campaign.createdBy (User) → CampaignDTO.createdBy (Long)
        mapper.addMappings(new PropertyMap<Campaign, CampaignDTO>() {
            @Override
            protected void configure() {
                map().setCreatedBy(source.getCreatedBy().getUserId());
            }
        });

        return mapper;
    }
}
