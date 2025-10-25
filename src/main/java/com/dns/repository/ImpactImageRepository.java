package com.dns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dns.repository.entity.ImpactImage;

import java.util.List;

@Repository
public interface ImpactImageRepository extends JpaRepository<ImpactImage, Long> {
    List<ImpactImage> findByStoryStoryId(Long storyId);
}
