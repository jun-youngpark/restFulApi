package com.mnwise.wiseu.web.common.ui.upload;

public interface OutputStreamListener {
    public void start();

    public void bytesRead(int bytesRead);

    public void error(String message);

    public void done();
}
