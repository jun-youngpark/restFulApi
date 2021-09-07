package com.mnwise.wiseu.web.common.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.mts.util.dto.MimeInfoDTO;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.campaign.dao.MimeInfoLogDao;
import com.mnwise.wiseu.web.campaign.dao.SendResultDao;
import com.mnwise.wiseu.web.common.model.MimeViewVo;
import com.mnwise.wiseu.web.common.util.ReadingMime;
import com.mnwise.wiseu.web.ecare.dao.EcareMimeInfoLogDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendResultDao;

@Service
public class MimeViewService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(MimeViewService.class);

    @Autowired private EcareMimeInfoLogDao ecareMimeInfoLogDao;
    @Autowired private EcareSendResultDao ecareSendResultDao;
    @Autowired private MimeInfoLogDao mimeInfoLogDao;
    @Autowired private SendResultDao sendResultDao;

    @Value("${mts.spool}")
    private String mtsSpoolPath;

    public Map<String, Object> getSuspendList(String schannelType, int serviceNo) throws Exception {
        Map<String, Object> retMap = new HashMap<>();
        Date date = new Date();
        List<MimeInfoDTO> suspendList = new ArrayList<>();

        int resultSeq = schannelType.equalsIgnoreCase("EM")
                      ? sendResultDao.emSuspendResultSeq(serviceNo)
                      : ecareSendResultDao.ecSuspendResultSeq(serviceNo);

        String filePath = mtsSpoolPath + "/mts/" + DateUtil.dateToString("yyyyMMdd", date) + "/MAIL/mime/" + schannelType.toLowerCase() + "/" + serviceNo;

        File[] file = new File(filePath).listFiles();

        for(int i = 0; i < file.length; i++) {
            String fileNm = file[i].getName();
            if(fileNm.indexOf(resultSeq) > -1 && fileNm.endsWith("info")) {
                try {
                    suspendList = listParse(file[i]);
                    retMap.put("tid", file[i].getName().substring(0, file[i].getName().indexOf("_")));
                    break;
                } catch(Exception e) {
                    log.debug(file[i].getPath() + " skip!  " + e.getMessage());
                }
            }
        }
        retMap.put("list", suspendList);

        return retMap;
    }

    private List<MimeInfoDTO> listParse(File file) throws Exception {
        List<MimeInfoDTO> retList = new ArrayList<>();
        BufferedReader in = null;
        String tmp = null;
        try {
            in = new BufferedReader(new FileReader(file));

            while((tmp = in.readLine()) != null) {
                retList.add(MimeInfoDTO.getParseDto(tmp));
            }
        } finally {
            IOUtil.closeQuietly(in);
        }
        return retList;
    }

    @SuppressWarnings("unchecked")
    public MimeViewVo getMimeInfo(MimeViewVo mimeVo) throws Exception {
        Date date = new Date();
        String filePath = mtsSpoolPath + "/mts/" + DateUtil.dateToString("yyyyMMdd", date) + "/MAIL/mime/" + mimeVo.getType().toLowerCase() + "/" + mimeVo.getServiceNo()
            + "/" + mimeVo.getTid() + "_" + mimeVo.getFileIdx() + "_" + mimeVo.getHanIdx() + ".mime";

        ReadingMime parse = new ReadingMime(filePath, Long.parseLong(mimeVo.getStartIndex()), Long.parseLong(mimeVo.getEndIndex()), mimeVo.getAttachFileNm());
        parse.mimeReading();
        parse.mimeParse();

        mimeVo.setMime(parse.getContent());
        mimeVo.setAttachFiles(parse.getAttachFiles());
        mimeVo.setFrom(parse.getFrom());
        mimeVo.setTo(parse.getTo());
        mimeVo.setSubject(parse.getSubject());

        return mimeVo;
    }

    public ReadingMime getMimeParse(MimeViewVo mimeVo) {
        return new ReadingMime(mimeVo.getMimePath(), Long.parseLong(mimeVo.getStartIndex()), Long.parseLong(mimeVo.getEndIndex()), mimeVo.getAttachFileNm());
    }

    @SuppressWarnings("unchecked")
    public String getAttachContent(MimeViewVo mimeVo, String sFileNm) throws Exception {
        ReadingMime parse = null;

        if(mimeVo.getResultSeq() > 0L) {
            parse = new ReadingMime(mimeVo.getMimePath(), Long.valueOf(mimeVo.getStartIndex()), Long.valueOf(mimeVo.getEndIndex()), sFileNm);
        } else {
            parse = new ReadingMime(mimeVo.getMimePath(), sFileNm);
        }

        parse.mimeReading();
        parse.mimeParse();

        mimeVo.setAttachFiles(parse.getAttachFiles());

        return (String) mimeVo.getAttachFiles().get(sFileNm);
    }

    public MimeViewVo getECMimeInfo(MimeViewVo mimeViewVo) throws Exception {
        return ecareMimeInfoLogDao.selectECMimeInfo(mimeViewVo);
    }

    public MimeViewVo getEMMimeInfo(MimeViewVo mimeVo) throws Exception {
        return mimeInfoLogDao.selectEMMimeInfo(mimeVo);
    }
}
