package com.lo.shop.utils;



import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;


public class CustomExceptionHandler implements UncaughtExceptionHandler{


    private UncaughtExceptionHandler defaultUEH;

    private String localPath;

    private String url;

    /*
     * if any of the parameters is null, the respective functionality
     * will not be used
     */
    public CustomExceptionHandler(String localPath, String url) {
        this.localPath = localPath;
        this.url = url;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    private void sendToServer(String stacktrace, String filename) {

    }

    public void uncaughtException(Thread t, Throwable e) {
        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        String filename = currentTimeStamp + ".txt";

        if (localPath != null) {
            writeToFile(stacktrace, filename);
        }
        if (url != null) {
            sendToServer(stacktrace, filename);
        }

        defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String stacktrace, String filename) {
        try {
            BufferedWriter bos = new BufferedWriter(new FileWriter(
                    localPath + "/" + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
