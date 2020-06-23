package com.xcheng.scanner.demo.scanner;

public interface ICameraAction {
    void create();

    void release();

    void flushLight(boolean v);

    void start();

    void stop();
}
