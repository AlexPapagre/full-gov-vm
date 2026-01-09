package gr.hua.dit.mycitygov.web.rest;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.mycitygov.core.service.MinioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestFileController {
    private final MinioService minioService;

    @GetMapping("/file/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            InputStream fileStream = minioService.getFile(fileName);
            InputStreamResource resource = new InputStreamResource(fileStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
    }
}
