package com.xcheng.scannere3;

public class XCScanner {
    static
    {
        // Load JNI share libraray
        System.loadLibrary("XCScanner");
    }

    // Java callbacks invoked by JNI
    public interface Result {
        // Called to play scan beep
        void scanBeep();

        // Called to start new scan round
        void scanStart();

        // Called to report scan result
        void scanResult(String sym,String content);
    }

    private XCScanner()
    {

    }

    // Create new scanner instance
    public native static XCScanner newInstance();

    // Delete scanner instance
    public native void deleteInstance();

    // Stop decode
    public native void stopDecode();

    // Start decode
    public native void startDecode();

    // Register scan listener callbacks
    public native XCScanner onScanListener(Result v);

    // Get version of JNI
    public native String getVersion();
}

