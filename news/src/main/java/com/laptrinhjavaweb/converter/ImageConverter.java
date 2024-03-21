package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.dto.ImageDTO;
import com.laptrinhjavaweb.entity.ImageEntity;
import org.springframework.stereotype.Component;

@Component
public class ImageConverter {
    public ImageDTO toDTO(ImageEntity entity) {
        ImageDTO dto = new ImageDTO();
        dto.setThumbnail(entity.getThumbnail());
        dto.setNewId(entity.getNewEntity().getId());
        // Sao chép các trường khác nếu cần
        return dto;
    }
}
