package com.example.akmourat.estitrack;

/**
 * Created by akmourat on 8/10/2016.
 */
public class ObjectConfig
{
    // All
    private static int m_Config = Constants.DEFAULT_PATTERN_INDEX;

    // Tracking
    // NONE

    // Counting
    private int m_Timeout = -1;

    // States
    private String m_VerticalUpString     = "Undefined";
    private String m_VerticalDownString   = "Undefined";
    private String m_VerticalLeftString   = "Undefined";
    private String m_VerticalRightString  = "Undefined";
    private String m_HorizontalUpString   = "Undefined";
    private String m_HorizontalDownString = "Undefined";

    // Rotation
    private int m_StartDegree     = -1;
    private int m_EndDegree       = -1;
    private int m_StartValue      = -1;
    private int m_EndValue        = -1;
    private boolean m_IsClockwise = true;

    // Temperature
    // NONE


    // Configure Counting Variables
    public void configCounting( int iTimeout )
    {
        m_Timeout = iTimeout;

        m_Config = Constants.TIMES_USED;
    }


    // Configure State Variables
    public void configStates( String sVertUp, String sVertDown, String sVertLeft, String sVertRight, String sHorizUp, String sHorizDown )
    {
        m_VerticalUpString     = sVertUp;
        m_VerticalDownString   = sVertDown;
        m_VerticalLeftString   = sVertLeft;
        m_VerticalRightString  = sVertRight;
        m_HorizontalUpString   = sHorizUp;
        m_HorizontalDownString = sHorizDown;

        if( sVertUp.isEmpty() )
            m_VerticalUpString = "No State";
        if( sVertDown.isEmpty() )
            m_VerticalDownString = "No State";
        if( sVertLeft.isEmpty() )
            m_VerticalLeftString = "No State";
        if( sVertRight.isEmpty() )
            m_VerticalRightString = "No State";
        if( sHorizUp.isEmpty() )
            m_HorizontalUpString = "No State";
        if( sHorizDown.isEmpty() )
            m_HorizontalDownString = "No State";

        m_Config = Constants.OBJECT_STATE;
    }


    // Configure Rotation Variables
    public void configRotation( int iStartDegree, int iEndDegree, int iStartVal, int iEndVal, boolean bIsClockwise )
    {
        m_StartDegree = iStartDegree;
        m_EndDegree   = iEndDegree;
        m_StartValue  = iStartVal;
        m_EndValue    = iEndVal;
        m_IsClockwise = bIsClockwise;

        m_Config = Constants.OBJECT_ROTATION;
    }


    // Get Config Type
    public int getConfigType(){ return m_Config; }


    // Get Counting Functions
    public int getTimeout(){ return m_Timeout; }


    // Get State Functions
    public String getVerticalUpString()    { return m_VerticalUpString; }
    public String getVerticalDownString()  { return m_VerticalDownString; }
    public String getVerticalLeftString()  { return m_VerticalLeftString; }
    public String getVerticalRightString() { return m_VerticalRightString; }
    public String getHorizontalUpString()  { return m_HorizontalUpString; }
    public String getHorizontalDownString(){ return m_HorizontalDownString; }


    // Get Rotation Functions
    public int getStartDegree()     { return m_StartDegree; }
    public int getEndDegree()       { return m_EndDegree; }
    public int getStartValue()      { return m_StartValue; }
    public int getEndValue()        { return m_EndValue; }
    public boolean getIsClockwise() { return m_IsClockwise; }
}
