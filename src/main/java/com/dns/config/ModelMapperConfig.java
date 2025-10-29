package com.dns.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dns.dto.CampaignDTO;
import com.dns.dto.UserDTO;
import com.dns.repository.entity.Campaign;
import com.dns.repository.entity.User;

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

        TypeMap<User, UserDTO> userToDtoTypeMap = mapper.createTypeMap(User.class, UserDTO.class);

        // Force override of automatic mapping for "role"
        userToDtoTypeMap.addMappings(m -> m.skip(UserDTO::setRole));
        userToDtoTypeMap.setPostConverter(context -> {
            User source = context.getSource();
            UserDTO destination = context.getDestination();
            if (source.getRoles() != null && !source.getRoles().isEmpty()) {
                destination.setRole(source.getRoles().get(0).getRoleName());
            } else {
                destination.setRole(null);
            }
            return destination;
        });
        return mapper;
    }
}
