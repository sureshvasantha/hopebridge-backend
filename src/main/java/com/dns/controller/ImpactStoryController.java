package com.dns.controller;

import com.dns.dto.ImpactStoryDTO;
import com.dns.service.ImpactStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admins/{adminId}/impact-stories")
@RequiredArgsConstructor
public class ImpactStoryController {

    private final ImpactStoryService impactStoryService;

    @PostMapping("/campaign/{campaignId}")
    public ResponseEntity<ImpactStoryDTO> createImpactStory(
            @PathVariable Long adminId,
            @PathVariable Long campaignId,
            @RequestPart("story") String storyJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles) throws IOException {
        ImpactStoryDTO created = impactStoryService.createImpactStory(adminId, campaignId, storyJson, imageFiles);
        return ResponseEntity.ok(created);
    }
}
