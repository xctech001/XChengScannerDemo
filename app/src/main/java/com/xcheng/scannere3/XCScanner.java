package com.xcheng.scannere3;

public class XCScanner {
    static
    {
        // Load JNI share libraray
        System.loadLibrary("XCScanner");
    }

    // Native scanner lights definition
    public static final int LIGHTS_ID_FLASH = 0;
    public static final int LIGHTS_ID_AIM = 1;

    // Native scanner workmode definition
    // SINGLESHOT - Auto-stop after scan success.
    // CONTINUOUS_SIMPLE - Simple continuous scan, not filter-out same result.
    // CONTINUOUS_DISTINCT - Continuous scan, same result only output once.
    public static final int WORKMODE_SINGLESHOT = 0;
    public static final int WORKMODE_CONTINUOUS_SIMPLE = 1;
    public static final int WORKMODE_CONTINUOUS_DISTINCT = 2;

    // Native code type definition
    public static final int E3_SYM_DOTCODE       = 0;
    public static final int E3_SYM_EAN13         = 1;
    public static final int E3_SYM_EAN8          = 2;
    public static final int E3_SYM_UPCA          = 3;
    public static final int E3_SYM_UPCE          = 4;
    public static final int E3_SYM_CODE11        = 5;
    public static final int E3_SYM_CODE32        = 6;
    public static final int E3_SYM_CODE39        = 7;
    public static final int E3_SYM_CODE93        = 8;
    public static final int E3_SYM_CODE128       = 9;
    public static final int E3_SYM_PDF417        = 10;
    public static final int E3_SYM_MICROPDF417   = 11;
    public static final int E3_SYM_QRCODE        = 12;
    public static final int E3_SYM_DATAMATRIX    = 13;
    public static final int E3_SYM_CODABAR       = 14;
    public static final int E3_SYM_AZTEC         = 15;
    public static final int E3_SYM_GS1_128       = 16;
    public static final int E3_SYM_GS1           = 17;
    public static final int E3_SYM_GS1_LIMITED   = 18;
    public static final int E3_SYM_GS1_EXPANDED  = 19;
    public static final int E3_SYM_TRIOPTIC      = 20;
    public static final int E3_SYM_ITF25         = 21;
    public static final int E3_SYM_MATRIX25      = 22;
    public static final int E3_SYM_IATA25        = 23;
    public static final int E3_SYM_INDUSTRIAL25  = 24;
    public static final int E3_SYM_CODABLOCK_F   = 25;
    public static final int E3_SYM_CODABLOCK_A   = 26;
    public static final int E3_SYM_MSI           = 27;
    public static final int E3_SYM_COMPOSITE     = 28;
    public static final int E3_SYM_TELEPEN       = 29;
    public static final int E3_SYM_MAXICODE      = 30;
    public static final int E3_SYM_HANXIN        = 31;
    public static final int E3_SYM_USPS_4_STATE  = 32;
    public static final int E3_SYM_HK25          = 33;
    public static final int E3_SYM_GRID_MATRIX   = 34;

    // Native decoder TAGs definitions
    // UPC-A
    public static final int TAG_UPCA_ENABLED                 = 0x1A010001;
    public static final int TAG_UPCA_CHECK_DIGIT_TRANSMIT    = 0x1A010002;
    public static final int TAG_UPCA_NUMBER_SYSTEM_TRANSMIT  = 0x1A010003;
    public static final int TAG_UPCA_2CHAR_ADDENDA_ENABLED	 = 0x1A010004;
    public static final int TAG_UPCA_5CHAR_ADDENDA_ENABLED	 = 0x1A010005;
    public static final int TAG_UPCA_ADDENDA_REQUIRED		 = 0x1A010006;
    public static final int TAG_UPCA_ADDENDA_SEPARATOR		 = 0x1A010007;
    public static final int TAG_UPCA_ADD_COUNTRY_CODE		 = 0x1A010008;

    // UPC-E
    public static final int TAG_UPCE0_ENABLED				 = 0x1A011001;
    public static final int TAG_UPCE1_ENABLED				 = 0x1A011002;
    public static final int TAG_UPCE_EXPAND					 = 0x1A011003;
    public static final int TAG_UPCE_CHECK_DIGIT_TRANSMIT	 = 0x1A011004;
    public static final int TAG_UPCE_NUMBER_SYSTEM_TRANSMIT	 = 0x1A011005;
    public static final int TAG_UPCE_2CHAR_ADDENDA_ENABLED	 = 0x1A011006;
    public static final int TAG_UPCE_5CHAR_ADDENDA_ENABLED	 = 0x1A011007;
    public static final int TAG_UPCE_ADDENDA_REQUIRED		 = 0x1A011008;
    public static final int TAG_UPCE_ADDENDA_SEPARATOR		 = 0x1A011009;

    // EAN-8
    public static final int TAG_EAN8_ENABLED                 = 0x1A012001;
    public static final int TAG_EAN8_CHECK_DIGIT_TRANSMIT    = 0x1A012002;
    public static final int TAG_EAN8_2CHAR_ADDENDA_ENABLED   = 0x1A012003;
    public static final int TAG_EAN8_5CHAR_ADDENDA_ENABLED   = 0x1A012004;
    public static final int TAG_EAN8_ADDENDA_REQUIRED        = 0x1A012005;
    public static final int TAG_EAN8_ADDENDA_SEPARATOR       = 0x1A012006;

    // EAN-13
    public static final int TAG_EAN13_ENABLED				 = 0x1A013001;
    public static final int TAG_EAN13_CHECK_DIGIT_TRANSMIT	 = 0x1A013002;
    public static final int TAG_EAN13_2CHAR_ADDENDA_ENABLED  = 0x1A013003;
    public static final int TAG_EAN13_5CHAR_ADDENDA_ENABLED  = 0x1A013004;
    public static final int TAG_EAN13_ADDENDA_REQUIRED		 = 0x1A013005;
    public static final int TAG_EAN13_ADDENDA_SEPARATOR		 = 0x1A013006;
    public static final int TAG_EAN13_ISBN_ENABLED			 = 0x1A013007;

    // Code 128
    public static final int TAG_CODE128_ENABLED              = 0x1A014001;
    public static final int TAG_CODE128_MIN_LENGTH           = 0x1A014002;
    public static final int TAG_CODE128_MAX_LENGTH           = 0x1A014003;
    public static final int TAG_C128_ISBT_ENABLED            = 0x1A014005;

    // GS1-128
    public static final int TAG_GS1_128_ENABLED              = 0x1A015001;
    public static final int TAG_GS1_128_MIN_LENGTH           = 0x1A015002;
    public static final int TAG_GS1_128_MAX_LENGTH           = 0x1A015003;

    //Code 39
    public static final int TAG_CODE39_ENABLED               = 0x1A016001;
    public static final int TAG_CODE39_MIN_LENGTH            = 0x1A016002;
    public static final int TAG_CODE39_MAX_LENGTH            = 0x1A016003;
    public static final int TAG_CODE39_CHECK_DIGIT_MODE      = 0x1A016004;
    public static final int TAG_CODE39_APPEND_ENABLED        = 0x1A016005;
    public static final int TAG_CODE39_FULL_ASCII_ENABLED    = 0x1A016006;
    public static final int TAG_CODE39_START_STOP_TRANSMIT   = 0x1A016007;
    public static final int TAG_CODE39_BASE32_ENABLED        = 0x1A016008;

    //TLC 39
    public static final int TAG_TLC39_ENABLED                = 0x1A017001;

    //Trioptic
    public static final int TAG_TRIOPTIC_ENABLED             = 0x1A018001;

    //Interleaved 2 of 5
    public static final int TAG_I25_ENABLED                  = 0x1A019001;
    public static final int TAG_I25_MIN_LENGTH               = 0x1A019002;
    public static final int TAG_I25_MAX_LENGTH               = 0x1A019003;
    public static final int TAG_I25_CHECK_DIGIT_MODE         = 0x1A019004;

    //Standard 2 of 5
    public static final int TAG_S25_ENABLED                  = 0x1A01A001;
    public static final int TAG_S25_MIN_LENGTH               = 0x1A01A002;
    public static final int TAG_S25_MAX_LENGTH               = 0x1A01A003;

    //IATA 2 of 5 (2 bar)
    public static final int TAG_IATA25_ENABLED               = 0x1A01B001;
    public static final int TAG_IATA25_MIN_LENGTH            = 0x1A01B002;
    public static final int TAG_IATA25_MAX_LENGTH            = 0x1A01B003;

    //Matrix 2 of 5
    public static final int TAG_M25_ENABLED                  = 0x1A01C001;
    public static final int TAG_M25_MIN_LENGTH               = 0x1A01C002;
    public static final int TAG_M25_MAX_LENGTH               = 0x1A01C003;
    public static final int TAG_M25_CHECK_DIGIT_MODE         = 0x1A01C004;

    //Code 93
    public static final int TAG_CODE93_ENABLED               = 0x1A01D001;
    public static final int TAG_CODE93_MIN_LENGTH            = 0x1A01D002;
    public static final int TAG_CODE93_MAX_LENGTH            = 0x1A01D003;

    //Code 11
    public static final int TAG_CODE11_ENABLED               = 0x1A01E001;
    public static final int TAG_CODE11_MIN_LENGTH            = 0x1A01E002;
    public static final int TAG_CODE11_MAX_LENGTH            = 0x1A01E003;
    public static final int TAG_CODE11_CHECK_DIGIT_MODE      = 0x1A01E004;

    //Codabar
    public static final int TAG_CODABAR_ENABLED              = 0x1A01F001;
    public static final int TAG_CODABAR_MIN_LENGTH           = 0x1A01F002;
    public static final int TAG_CODABAR_MAX_LENGTH	         = 0x1A01F003;
    public static final int TAG_CODABAR_START_STOP_TRANSMIT	 = 0x1A01F004;
    public static final int TAG_CODABAR_CHECK_DIGIT_MODE	 = 0x1A01F005;
    public static final int TAG_CODABAR_CONCAT_ENABLED		 = 0x1A01F007;

    //Telepen
    public static final int TAG_TELEPEN_ENABLED		         = 0x1A020001;
    public static final int TAG_TELEPEN_MIN_LENGTH           = 0x1A020002;
    public static final int TAG_TELEPEN_MAX_LENGTH           = 0x1A020003;

    //MSI
    public static final int TAG_MSI_ENABLED			         = 0x1A021001;
    public static final int TAG_MSI_MIN_LENGTH		         = 0x1A021002;
    public static final int TAG_MSI_MAX_LENGTH		         = 0x1A021003;
    public static final int TAG_MSI_CHECK_DIGIT_MODE         = 0x1A021004;

    //RSS (GS1)
    public static final int TAG_RSS_14_ENABLED			     = 0x1A022001;
    public static final int TAG_RSS_LIMITED_ENABLED		     = 0x1A022002;
    public static final int TAG_RSS_EXPANDED_ENABLED	     = 0x1A022003;
    public static final int TAG_RSS_EXPANDED_MIN_LENGTH      = 0x1A022004;
    public static final int TAG_RSS_EXPANDED_MAX_LENGTH      = 0x1A022005;

    //Codablock F
    public static final int TAG_CODABLOCK_F_ENABLED          = 0x1A023001;
    public static final int TAG_CODABLOCK_F_MIN_LENGTH       = 0x1A023002;
    public static final int TAG_CODABLOCK_F_MAX_LENGTH       = 0x1A023003;

    //PDF417
    public static final int TAG_PDF417_ENABLED	             = 0x1A024001;
    public static final int TAG_PDF417_MIN_LENGTH            = 0x1A024002;
    public static final int TAG_PDF417_MAX_LENGTH            = 0x1A024003;

    //Micro PDF417
    public static final int TAG_MICROPDF_ENABLED	         = 0x1A025001;
    public static final int TAG_MICROPDF_MIN_LENGTH          = 0x1A025002;
    public static final int TAG_MICROPDF_MAX_LENGTH          = 0x1A025003;

    //Composite
    public static final int TAG_COMPOSITE_ENABLED	         = 0x1A026001;
    public static final int TAG_COMPOSITE_MIN_LENGTH         = 0x1A026002;
    public static final int TAG_COMPOSITE_MAX_LENGTH         = 0x1A026003;

    //Aztec Code
    public static final int TAG_AZTEC_ENABLED	             = 0x1A027001;
    public static final int TAG_AZTEC_MIN_LENGTH             = 0x1A027002;
    public static final int TAG_AZTEC_MAX_LENGTH             = 0x1A027003;

    //Maxicode
    public static final int TAG_MAXICODE_ENABLED	         = 0x1A028001;
    public static final int TAG_MAXICODE_MIN_LENGTH          = 0x1A028002;
    public static final int TAG_MAXICODE_MAX_LENGTH          = 0x1A028003;

    //DataMatrix
    public static final int TAG_DATAMATRIX_ENABLED	         = 0x1A029001;
    public static final int TAG_DATAMATRIX_MIN_LENGTH        = 0x1A029002;
    public static final int TAG_DATAMATRIX_MAX_LENGTH        = 0x1A029003;

    //QR Code
    public static final int TAG_QR_ENABLED	                 = 0x1A02A001;
    public static final int TAG_QR_MIN_LENGTH                = 0x1A02A002;
    public static final int TAG_QR_MAX_LENGTH                = 0x1A02A003;

    //HanXin
    public static final int TAG_HANXIN_ENABLED	             = 0x1A02B001;
    public static final int TAG_HANXIN_MIN_LENGTH            = 0x1A02B002;
    public static final int TAG_HANXIN_MAX_LENGTH            = 0x1A02B003;

    //Hong Kong 2 of 5 - aka China Post
    public static final int TAG_HK25_ENABLED	             = 0x1A02C001;
    public static final int TAG_HK25_MIN_LENGTH              = 0x1A02C002;
    public static final int TAG_HK25_MAX_LENGTH              = 0x1A02C003;

    //NEC 2 of 5
    public static final int TAG_NEC25_ENABLED                = 0x1A02F001;
    public static final int TAG_NEC25_MIN_LENGTH             = 0x1A02F002;
    public static final int TAG_NEC25_MAX_LENGTH             = 0x1A02F003;
    public static final int TAG_NEC25_CHECK_DIGIT_MODE       = 0x1A02F004;

    //Codablock A
    public static final int TAG_CODABLOCK_A_ENABLED	         = 0x1A030001;
    public static final int TAG_CODABLOCK_A_MIN_LENGTH       = 0x1A030002;
    public static final int TAG_CODABLOCK_A_MAX_LENGTH       = 0x1A030003;

    //Postal code
    public static final int TAG_POSTAL_ENABLED               = 0x1A110001;

    //Code128 misc
    public static final int TAG_C128_PARTIAL_OUTPUT          = 0x1B014002;
    public static final int TAG_C128_SHORT_MARGIN            = 0x1B014003;
    public static final int TAG_C128_SECURITY_LEVEL          = 0x1B014005;
    public static final int TAG_C128_PPM_MIN                 = 0x1B014006;
    public static final int TAG_C128_PPM_MAX                 = 0x1B014007;
    public static final int TAG_CODE128_INVERSE              = 0x1B014008;

    //DataMatrix misc
    public static final int TAG_DATAMATRIX_MIN_MODULE_COUNT	 = 0x1B029001;
    public static final int TAG_DATAMATRIX_MAX_MODULE_COUNT	 = 0x1B029002;
    public static final int TAG_DATAMATRIX_ROI_SEARCH		 = 0x1B029010;
    public static final int TAG_DATAMATRIX_DPM_SEARCH		 = 0x1B029011;
    public static final int TAG_DATAMATRIX_EDGE_THRES		 = 0x1B029012;
    public static final int TAG_DATAMATRIX_RECT_FIXED        = 0x1B029013;
    public static final int TAG_DATAMATRIX_RECT_TYPE         = 0x1B029014;
    public static final int TAG_DATAMATRIX_NON_ELL           = 0x1B029015;
    public static final int TAG_DATAMATRIX_DIRECTION         = 0x1B029016;

    //Grid Matrix
    public static final int TAG_GM_ENABLED	                 = 0x1C036001;
    public static final int TAG_GM_MIN_LENGTH                = 0x1C036002;
    public static final int TAG_GM_MAX_LENGTH                = 0x1C036003;

    //Dot code
    public static final int TAG_DOT_ENABLE                   = 0x1C03C001;

    // Native scanner lights test mode definition
    // Notice: Only test function, do not use it to application
    public static final int LIGHT_TEST_MODE_TORCH_ON  = 0;
    public static final int LIGHT_TEST_MODE_TORCH_OFF = 1;
    public static final int LIGHT_TEST_MODE_FLASH     = 2;
    public static final int LIGHT_TEST_MODE_AIM_ON    = 3;
    public static final int LIGHT_TEST_MODE_AIM_OFF   = 4;
    public static final int LIGHT_TEST_MODE_RESET     = 5;

    // Java callbacks invoked by JNI
    public interface Result {
        // Be called firstly after scan finish to play beep
        // The beep notification should be implemented by user
        void scanBeep();

        // Be called secondary after scan finish to start next scan round.
        // The scan start should be implemented by user and use the
        // #startDecode to start next scan round.
        void scanStart();

        // Be called thirdly after scan finish to report scan result
        // Should not update UI in this implement.
        void scanResult(String sym, String content);
    }

    private XCScanner()
    {

    }

    // Create new scanner instance
    public native static XCScanner newInstance();

    // Delete scanner instance
    public native void deleteInstance();

    // Stop current decode session
    public native void stopDecode();

    // Start decode session
    public native void startDecode();

    // Register scan listener callbacks
    public native XCScanner onScanListener(Result v);

    // Disable all code types
    public native void disableAllSym();

    // Enable all code types
    public native void enableAllSym();

    // Enable or disable specific code type
    public native void configSymOnOff(int symid, int onoff);

    // Set max timeout of each round
    public native void setRoundTimeout(int timeMs);

    // Config decoder tag settings
    public native void configDecoderTag(int tag, int val);

    // Get value of decoder tag
    public native int getDecoderTag(int tag);

    // Set scanner workmode
    // (SINGLESHOT/CONTINUOUS_SIMPLE/CONTINUOUS_DISTINCT)
    public native void setWorkMode(int workmode);

    // Get scanner workmode
    public native int getWorkMode();

    // Enable or disable lights control
    // This API is not to control the Lights to on/off.
    // It is used to config lights controlled or uncontrolled by lower decode engine.
    public native void configLights(int lightsId, int supported);

    // Set light testmode
    // Notice: Only test function, do not use it to application
    public native void setLightTestMode(int testmode);

    // Get version of this SDK.
    public native String getVersion();
}

