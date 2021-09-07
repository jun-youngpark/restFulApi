package com.mnwise.wiseu.web.rest.model.contents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAttachMent {

        private String originalFileName;
        private String filePath;

        public String getOriginalFileName() {
            return originalFileName;
        }
        public void setOriginalFileName(String originalFileName) {
            this.originalFileName = originalFileName;
        }
        public String getFilePath() {
            return filePath;
        }
        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

}
