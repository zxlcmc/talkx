package org.bigmouth.gpt.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.service.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author tangxiao
 * @date 2023/7/20
 * @since 1.0
 */
@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Override
    public String uploadImg(MultipartFile file) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String uploadImg(MultipartFile file, String parentDir) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String uploadFile(MultipartFile file) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String uploadFile(MultipartFile file, String parentDir) {
        throw new UnsupportedOperationException();
    }
}
