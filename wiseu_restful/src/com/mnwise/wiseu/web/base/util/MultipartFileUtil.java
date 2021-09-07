package com.mnwise.wiseu.web.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mnwise.common.io.IOUtil;

public class MultipartFileUtil {

    public static MultipartFile fileToMultipartFileConvert(String filePath, String fileName) throws Exception{
        File file = new File(filePath);
        FileItem fileItem = null ;
        InputStream in = null ;
        OutputStream out = null ;
        try {
            fileItem = new DiskFileItem(fileName, Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
            in = new FileInputStream(file);
            out = fileItem.getOutputStream();
            IOUtils.copy(in, out);
        } catch (IOException ex) {
            throw new Exception(ex);
        }finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(out);
        }
        return new CommonsMultipartFile(fileItem);
    }

}
