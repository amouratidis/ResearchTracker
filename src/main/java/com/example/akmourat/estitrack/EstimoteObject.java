package com.example.akmourat.estitrack;

/**
 * Created by akmourat on 6/16/2016.
 */
public class EstimoteObject
{
    private String sEstimoteID;
    private String sReadableID;
    private String sObjectName;
    private int iPattern;

    private ObjectConfig m_Config;

    // Construct the Estimote Object
    EstimoteObject( String sID, String sName, int iPatternIndex, ObjectConfig config )
    {
        sEstimoteID = sID;
        sObjectName = sName;
        sReadableID = KeyList.getNameById( sID );
        iPattern    = iPatternIndex;
        m_Config    = config;
    }


    // Empty Constructor
    EstimoteObject()
    {
    }


    public ObjectConfig getConfig()
    {
        return m_Config;
    }


    @Override
    public String toString()
    {
        return sReadableID;
    }


    // -- Get Functions -- //


    // Return the Estimote ID
    public String getEstimoteID()
    {
        return sEstimoteID;
    }

    // Return the Readable ID
    public String getReadableID()
    {
        return sReadableID;
    }

    // Return the Object Name
    public String getObjectName()
    {
        return sObjectName;
    }

    // Return the Pattern Type
    public int getPattern()
    {
        return iPattern;
    }

    // Set the Estimote ID
    public void setEstimoteID( String sID )
    {
        sEstimoteID = sID;
        sReadableID = KeyList.getNameById( sID );
    }

    // Set the Object Name
    public void setObjectName( String sName )
    {
        sObjectName = sName;
    }

    // Set the Pattern Type - Requires Index to Pattern Array
    public void setPattern( int iPatternIndex )
    {
        if( iPatternIndex < Constants.PATTERNS.length )
            iPattern = iPatternIndex;
    }
}
