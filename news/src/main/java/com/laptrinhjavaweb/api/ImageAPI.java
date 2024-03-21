package com.laptrinhjavaweb.api;

import com.laptrinhjavaweb.dto.ImageDTO;
import com.laptrinhjavaweb.entity.ImageEntity;
import com.laptrinhjavaweb.service.INewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class ImageAPI {
    @Autowired
    private INewService newService;

    /*@GetMapping("/all-image")
    public ResponseEntity<List<ImageEntity>> getAllImages() {
        List<ImageEntity> imageEntities = newService.getAllImage();
        return ResponseEntity.ok(imageEntities);
    }*/
}
