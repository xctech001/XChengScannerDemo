package com.xcheng.scanner.demo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import com.xcheng.scanner.demo.utils.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class PreviewView extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    public interface PreviewListener {
        void onViewCreate(SurfaceTexture v);

        void onViewChange(SurfaceTexture v);

        void onViewPause();
    }

    private static final int VERSION_CLIENT = 2;

    private final static String SHADER_VERTEX = ""
            + "attribute vec2 vPosition;\n"
            + "attribute vec2 vTexCoord;\n"
            + "varying vec2 texCoord;\n"
            + "void main() {\n"
            + "  texCoord = vTexCoord;\n"
            + "  gl_Position = vec4 (vPosition.x, vPosition.y, 0.0, 1.0);\n"
            + "}";

    private final static String SHADER_FRAGMENT = ""
            + "#extension GL_OES_EGL_image_external : require\n"
            + "precision mediump float;\n"
            + "uniform samplerExternalOES sTexture;\n"
            + "varying vec2 texCoord;\n"
            + "void main() {\n"
            + "  gl_FragColor = texture2D(sTexture,texCoord);\n"
            + "}";

    private final String SHADER_FSSTEX2SCREEN = ""
            + "precision mediump float;\n"
            + "uniform sampler2D sTexture;\n"
            + "varying vec2 texCoord;\n"
            + "void main() {\n"
            + "  gl_FragColor = texture2D(sTexture,texCoord);\n" + "}";

    private static final float[] TXT_VERTICES = {
            -1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f,
    };

    private static final float[] TXT_COORDS = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    private int mFBO;
    private int mCamTextureID;
    private int mFboTextureID;
    private int mDrawTextureID;
    private int mProgCam2FBO;
    private int mProgTex2Screen;

    private int mVcCam2FBO;
    private int mTcCam2FBO;
    private int mVcTex2Screen;
    private int mTcTex2Screen;

    private int mPreviewWidth;
    private int mPreviewHeight;

    private FloatBuffer mTxtCoorBuffer;
    private FloatBuffer mTxtVertBuffer;
    private SurfaceTexture mSurfaceTexture;
    private PreviewListener mPreviewListener;

    private final Handler mListenerHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mPreviewListener == null)
                return;

            switch (msg.what) {
                case 0:
                    mPreviewListener.onViewCreate(mSurfaceTexture);
                    break;
                case 1:
                    mPreviewListener.onViewChange(mSurfaceTexture);
                    break;
                case 2:
                    mPreviewListener.onViewPause();
                    break;
            }
        }
    };

    public PreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(VERSION_CLIENT);
        this.setRenderer(this);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        int[] texture = new int[1];

        GLES20.glGenTextures(texture.length, texture, 0);
        this.mCamTextureID = texture[texture.length - 1];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, this.mCamTextureID);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        this.mSurfaceTexture = new SurfaceTexture(this.mCamTextureID);
        this.mSurfaceTexture.setOnFrameAvailableListener(this);

        int len = TXT_COORDS.length * Float.SIZE / Byte.SIZE;
        ByteBuffer coor = ByteBuffer.allocateDirect(len).order(ByteOrder.nativeOrder());
        this.mTxtCoorBuffer = coor.asFloatBuffer();
        this.mTxtCoorBuffer.put(TXT_COORDS).position(0);

        ByteBuffer vert = ByteBuffer.allocateDirect(len).order(ByteOrder.nativeOrder());
        this.mTxtVertBuffer = vert.asFloatBuffer();
        this.mTxtVertBuffer.put(TXT_VERTICES).position(0);

        int shaderVert = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(shaderVert, SHADER_VERTEX);
        GLES20.glCompileShader(shaderVert);

        int shaderFrag = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(shaderFrag, SHADER_FRAGMENT);
        GLES20.glCompileShader(shaderFrag);

        int shaderFragScreen = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(shaderFragScreen, SHADER_FSSTEX2SCREEN);
        GLES20.glCompileShader(shaderFragScreen);

        this.mProgCam2FBO = GLES20.glCreateProgram();
        GLES20.glAttachShader(this.mProgCam2FBO, shaderVert);
        GLES20.glAttachShader(this.mProgCam2FBO, shaderFrag);
        GLES20.glLinkProgram(this.mProgCam2FBO);

        this.mVcCam2FBO = GLES20.glGetAttribLocation(this.mProgCam2FBO, "vPosition");
        this.mTcCam2FBO = GLES20.glGetAttribLocation(this.mProgCam2FBO, "vTexCoord");
        GLES20.glEnableVertexAttribArray(this.mVcCam2FBO);
        GLES20.glEnableVertexAttribArray(this.mTcCam2FBO);

        this.mProgTex2Screen = GLES20.glCreateProgram();
        GLES20.glAttachShader(this.mProgTex2Screen, shaderVert);
        GLES20.glAttachShader(this.mProgTex2Screen, shaderFragScreen);
        GLES20.glLinkProgram(this.mProgTex2Screen);

        this.mVcTex2Screen = GLES20.glGetAttribLocation(this.mProgTex2Screen, "vPosition");
        this.mTcTex2Screen = GLES20.glGetAttribLocation(this.mProgTex2Screen, "vTexCoord");
        GLES20.glEnableVertexAttribArray(this.mVcTex2Screen);
        GLES20.glEnableVertexAttribArray(this.mTcTex2Screen);

        this.mListenerHandler.sendEmptyMessage(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        int[] id = new int[1];

        this.mPreviewWidth = width;
        this.mPreviewHeight = height;
        Log.info("opengl width: %d, height: %d", width, height);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        id[id.length - 1] = this.mFBO;
        GLES20.glDeleteFramebuffers(id.length, id, 0);
        id[id.length - 1] = this.mDrawTextureID;
        GLES20.glDeleteTextures(id.length, id, 0);
        id[id.length - 1] = this.mFboTextureID;
        GLES20.glDeleteTextures(id.length, id, 0);

        GLES20.glGenTextures(id.length, id, 0);
        this.mDrawTextureID = id[id.length - 1];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.mDrawTextureID);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        GLES20.glGenTextures(id.length, id, 0);
        this.mFboTextureID = id[id.length - 1];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.mFboTextureID);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        GLES20.glGenFramebuffers(id.length, id, 0);
        this.mFBO = id[id.length - 1];
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.mFBO);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
                this.mFboTextureID, 0);

        this.mListenerHandler.sendEmptyMessage(1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        float[] matrix = new float[16];
        this.mSurfaceTexture.updateTexImage();
        this.mSurfaceTexture.getTransformMatrix(matrix);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.mFBO);
        GLES20.glViewport(0, 0, this.mPreviewWidth, this.mPreviewHeight);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(this.mProgCam2FBO);
        GLES20.glVertexAttribPointer(this.mVcCam2FBO, 2, GLES20.GL_FLOAT, false, 4 * 2, this.mTxtVertBuffer);

        this.mTxtCoorBuffer.clear();
        this.mTxtCoorBuffer.put(transformTextureCoordinates(TXT_COORDS, matrix));
        this.mTxtCoorBuffer.position(0);

        GLES20.glVertexAttribPointer(this.mTcCam2FBO, 2, GLES20.GL_FLOAT, false, 4 * 2, this.mTxtCoorBuffer);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, this.mCamTextureID);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(this.mProgCam2FBO, "sTexture"), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glFlush();

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glViewport(0, 0, this.mPreviewWidth, this.mPreviewHeight);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(this.mProgTex2Screen);
        GLES20.glVertexAttribPointer(this.mVcTex2Screen, 2, GLES20.GL_FLOAT, false, 4 * 2, this.mTxtVertBuffer);

        this.mTxtCoorBuffer.clear();
        this.mTxtCoorBuffer.put(transformTextureCoordinates(TXT_COORDS, matrix));
        this.mTxtCoorBuffer.position(0);

        GLES20.glVertexAttribPointer(this.mTcTex2Screen, 2, GLES20.GL_FLOAT, false, 4 * 2, this.mTxtCoorBuffer);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.mFboTextureID);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(this.mProgTex2Screen, "sTexture"), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glFlush();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.requestRender();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mListenerHandler.sendEmptyMessage(2);
    }

    public void setListener(PreviewListener v) {
        this.mPreviewListener = v;
    }

    private float[] transformTextureCoordinates(float[] coords, float[] matrix) {
        float[] result = new float[coords.length];
        float[] vt = new float[4];
        for (int i = 0; i < coords.length; i += 2) {
            float[] v = {coords[i], coords[i + 1], 0, 1};
            Matrix.multiplyMV(vt, 0, matrix, 0, v, 0);
            result[i] = vt[0];
            result[i + 1] = vt[1];
        }
        return result;
    }
}
