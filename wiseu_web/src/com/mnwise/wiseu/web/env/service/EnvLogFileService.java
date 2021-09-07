package com.mnwise.wiseu.web.env.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.ZipUtil;

@Service
public class EnvLogFileService extends BaseService {
    @Value("${log.download.dir}")
    private String logHomeDirs;

    /**
     * 경로내의 폴더, 파일 읽어오기
     *
     * @param selectedIndex, currentDir
     * @return dirReturnDataMap
     */
    public Map<String, Object> getDirFileList(int logDirIndex, String requestDir) throws Exception {
        Map<String, Object> dirReturnDataMap = new HashMap<>();
        dirReturnDataMap.put("rootFolderIndex", logDirIndex);

        if(StringUtil.isBlank(logHomeDirs)) {
            return dirReturnDataMap;
        }

        String[] logHomeDirArray = logHomeDirs.split(";");
        List<Map<String, Object>> homeDirMapList = new ArrayList<>();
        List<String> homeParentDirList = new ArrayList<>();

        // 프로퍼티에 등록 된 폴더 구분
        for(int i = 0; i < logHomeDirArray.length; i++) {
            if(logDirIndex == i && requestDir.equals("")) {
                requestDir = logHomeDirArray[i];
            }

            File homeDir = new File(logHomeDirArray[i]);

            Map<String, Object> homeDirMap = new HashMap<>();
            homeDirMap.put("folderIndex", i);
            homeDirMap.put("folderName", homeDir.getName());

            homeParentDirList.add(homeDir.getParent().replace("\\", "/"));

            homeDirMapList.add(homeDirMap);
        }

        //folderPath경로 내 폴더와 파일 명의 정렬
        File dir = new File(requestDir);

        String currentParentDir = null;
        // homeParentDirList 내 없는 것은 보안상 무시하도록 처리
        for(String tempParentDir : homeParentDirList) {
            if((dir.getParent().replace("\\", "/")).equals(tempParentDir)) {
                currentParentDir = tempParentDir;
                break;
            }
        }

        String parentDir = null;
        String dirPath = null;
        // 탭에 지정된 폴더의 상위폴더는 따로 담고, 그 외 하위 디렉토리 읽어오기
        if(requestDir.equals(currentParentDir)) {
            parentDir = currentParentDir;
        } else {
            parentDir = dir.getParent();
            List<Map<String, Object>> fileMapList = new ArrayList<>();
            List<Map<String, Object>> subDirMapList = new ArrayList<>();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            File[] fileArray = dir.listFiles();
            Arrays.sort(fileArray);
            for(File file : fileArray) {
                dirPath = file.getPath().replace("\\", "/"); // 폴더경로

                if(file.isDirectory()) {
                    Map<String, Object> subDirMap = new HashMap<>();
                    subDirMap.put("folder", dirPath);
                    subDirMap.put("folderName", file.getName()); // 폴더명
                    subDirMap.put("folderDate", formatter.format(file.lastModified())); // 최근수정일

                    subDirMapList.add(subDirMap);
                } else if(file.isFile()) {
                    Map<String, Object> fileMap = new HashMap<>();
                    fileMap.put("filePath", dirPath); // 파일경로
                    fileMap.put("fileName", file.getName()); // 파일명
                    fileMap.put("fileSize", (file.length() / (long) 1024 + 1)); // 파일사이즈
                    fileMap.put("fileDate", formatter.format(file.lastModified())); // 최근수정일

                    fileMapList.add(fileMap);
                }
            }

            dirReturnDataMap.put("subDirMapList", subDirMapList);
            dirReturnDataMap.put("currentDir", requestDir);
            dirReturnDataMap.put("fileMapList", fileMapList);
        }

        dirReturnDataMap.put("currentParentDir", currentParentDir);
        dirReturnDataMap.put("parentDir", parentDir.replace("\\", "/"));
        dirReturnDataMap.put("rootFolderList", homeDirMapList);

        return dirReturnDataMap;
    }

    /**
     * 지정된 폴더를 Zip 파일로 압축한다.
     *
     * @param filePaths - 압축 대상 디렉토리
     * @throws Exception
     */

    public File downloadZip(String filePaths, File destDir) throws Exception {
        FileUtil.forceMkdir(destDir);

        StringTokenizer tokens = new StringTokenizer(filePaths.replace("\\", "/"), "╊");

        while(tokens.hasMoreElements()) {
            FileUtil.copyFileToDirectory(new File(tokens.nextToken()), destDir); // 임시 파일 복사
        }

        return makeZip(destDir);
    }

    /**
     * 지정된 폴더를 Zip 파일로 압축한다.
     *
     * @param zipDir - 압축 대상 디렉토리
     * @throws Exception
     */
    public File makeZip(File zipDir) throws Exception {
        File zipFile = new File(StringUtil.escapeFilePath(zipDir.getAbsolutePath() + ".zip"));

        ZipUtil.compressZip(FileUtil.listFiles(zipDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE), zipFile.getAbsolutePath());

        return zipFile;
    }
}
