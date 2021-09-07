package com.mnwise.wiseu.web.common.upload.formobjects;

import org.springframework.web.multipart.MultipartFile;

public class DataImportForm {
    private MultipartFile file;

    public final MultipartFile getFile() {
        return file;
    }

    public final void setFile(final MultipartFile file) {
        this.file = file;
    }

}
