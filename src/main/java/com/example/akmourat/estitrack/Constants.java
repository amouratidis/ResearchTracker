package com.example.akmourat.estitrack;

/**
 * Created by akmourat on 7/18/2016.
 */
public class Constants
{
    // Default Pattern Index
    public static final int DEFAULT_PATTERN_INDEX = 0;

    // Pattern Indices
    public static final int MOTION_TRACKING    = 0;
    public static final int TIMES_USED         = 1;
    public static final int OBJECT_STATE       = 2;
    public static final int OBJECT_ROTATION    = 3;
    public static final int OBJECT_TEMPERATURE = 4;

    // Tab Indices
    public static final int EMPTY_TAB    = 0;
    public static final int COUNT_TAB    = 1;
    public static final int STATE_TAB    = 2;
    public static final int ROTATION_TAB = 3;

    // Pattern Strings
    public static final String[] PATTERNS =
    {
        "Motion Tracking",
        "Times Used",
        "Object State",
        "Object Rotation",
        "Object Temperature"
    };

    // Detected Strings
    public static final String DETECTED   = "Detected";
    public static final String UNDETECTED = "Undetected";
}
