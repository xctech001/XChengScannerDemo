package com.xcheng.scanner.demo.scanner.common;

import java.util.List;

import android.hardware.Camera;

public class UtilsCamera {
    private static final double ASPECT_TOLERANCE = 0.1;

    public static int getCameraOneDisplayFps(Camera.Parameters parameters) {
        int fpsRet = 0;
        List<int[]> support = parameters.getSupportedPreviewFpsRange();

        for (int[] a : support) {
            for (int b : a) {
                if (b > fpsRet)
                    fpsRet = b;
            }
        }
        return fpsRet;
    }

    public static Camera.Size getCameraOneOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - h);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - h);
                }
            }
        }
        return optimalSize;
    }
}
