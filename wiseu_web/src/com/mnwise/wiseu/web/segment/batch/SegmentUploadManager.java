package com.mnwise.wiseu.web.segment.batch;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.util.BaseThreadFactory;
import com.mnwise.wiseu.web.base.util.BaseThreadPoolExecutor;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.service.SegmentService;

/**
 * 대량의 대상자 등록시 웹에서 올려진 대상자 파일을 nvfileupload 테이블에 백그라운드로 등록 처리하기 위한 배치 매니져 클래스
 * nvsegment 테이블의 segment_sts 컬럼이 사용되고 있지 않아서 해당 컬럼을 사용
 */
public class SegmentUploadManager extends Thread implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(SegmentUploadManager.class);

    public static ThreadPoolExecutor executor = null;
    final LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>(10);

    private SegmentService segmentService;
    private int executePeriod;
    private ApplicationContext ctx;

    public void setSegmentService(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    public void setExecutePeriod(int executePeriod) {
        this.executePeriod = executePeriod;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public SegmentUploadManager(int corePoolSize, int maximumPoolSize) {
        if(log.isDebugEnabled()) {
            log.debug("SegmentUploadManager Start.. ThreadPool[" + corePoolSize + ", " + maximumPoolSize + "]");
        }

        setDaemon(true);
        executor = new BaseThreadPoolExecutor(corePoolSize, maximumPoolSize, 100L, TimeUnit.MILLISECONDS, blockingQueue, new BaseThreadFactory("SfdcBatchSend"));
    }

    public void run() {
        int targetNo;
        String sTemp = null;

        // 서버가 비정상 종료되거나 했을 경우를 대비해서 업로드가 완료되지 않은 세그먼트 목록을
        // 조회해서 이미 업로드된 데이타를 삭제하고 세그먼트의 업로드 상태값을 대기상태로 변경한다.
        // 여기가 nv삭제하고
        List<SegmentVo> list = null;
        try {
            list = segmentService.getUploadRunListForAsync();
            for(SegmentVo segmentVo : list) {
                sTemp = segmentVo.getSqlBody();
                sTemp = sTemp.substring(sTemp.lastIndexOf("=") + 1);
                targetNo = Integer.parseInt(sTemp.trim());

                // targetNo 에 해당하는 대상자 데이터 삭제
                segmentService.deleteTargetData(targetNo);

                // NVSEGMENT테이블에 대기 상태로 업데이트
                segmentVo.setSegmentSts(Const.SEGMENT_STS_UPLOAD_WAIT);
                segmentService.updateSegmentForSegmentSts(segmentVo);
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }

        while(true) {
            try {
                TimeUnit.SECONDS.sleep(executePeriod);

                // 업로드를 실행해야 할 목록
                list = segmentService.getSegmentListForAsync();

                for(SegmentVo segmentVo : list) {
                    SegmentUploadWorker worker = (SegmentUploadWorker) ctx.getBean("segmentUploadWorker");
                    sTemp = segmentVo.getSqlBody();
                    sTemp = sTemp.substring(sTemp.lastIndexOf("=") + 1);
                    targetNo = Integer.parseInt(sTemp.trim());

                    segmentVo.setSegmentSts(Const.SEGMENT_STS_UPLOAD_RUN);
                    segmentVo.setTargetNo(targetNo);

                    worker.setSegmentVo(segmentVo);
                    segmentService.updateSegmentForSegmentSts(segmentVo);

                    executor.execute(worker);
                }

                if(log.isDebugEnabled()) {
                    log.debug("SegmentUpload Status..Active : " + executor.getActiveCount() + " / Maximum : " + executor.getMaximumPoolSize());
                }
            } catch(Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
