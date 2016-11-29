package com.example.akmourat.estitrack;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akmourat on 6/16/2016.
 */
public class EstimoteDataManager
{
    private static final String XML_FILENAME = "EstimoteData";
    private static File m_xmlFile;
    private static Context m_Context;


    // Set the Context - Must be done at start
    public static void setContext( Context appContext )
    {
        // Create the XML File
        m_xmlFile = new File( appContext.getFilesDir(), XML_FILENAME + ".xml" );

        // Set the Context for later use
        m_Context = appContext;
    }


    // Uses getAllObjects to return a specific object
    public static EstimoteObject getObject( int iObjectIndex )
    {
        List<EstimoteObject> objectList = getAllObjectsInUse();

        return objectList.get( iObjectIndex );
    }


    // Uses getAllObjects to return a specific index
    public static int getIndex( EstimoteObject currObject )
    {
        List<EstimoteObject> objectList = getAllObjectsInUse();

        int iIndexFound = -1;
        for( int iObjectIndex = 0; iObjectIndex < objectList.size(); iObjectIndex++ )
        {
            if( objectList.get( iObjectIndex ).getEstimoteID().equals( currObject.getEstimoteID() ) )
            {
                iIndexFound = iObjectIndex;
                break;
            }
        }

        return iIndexFound;
    }


    // Gets all Objects that have been set by the user
    public static List<EstimoteObject> getAllObjectsInUse()
    {
        List<EstimoteObject> objectList = new ArrayList<EstimoteObject>();

        try
        {
            // Create the Parser
            XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlParser = xmlFactory.newPullParser();
            xmlParser.setInput( new FileInputStream( m_xmlFile ), "UTF-8" );

            // Loop through All Events
            int xmlEvent    = xmlParser.getEventType();
            String sTagText = "";

            // Object Vars
            String sCurrID   = "";
            String sCurrName = "";
            int iCurrPattern = Constants.DEFAULT_PATTERN_INDEX;

            // Config Vars
            int iTimeout         = 0;
            String sVertUp       = "";
            String sVertDown     = "";
            String sVertLeft     = "";
            String sVertRight    = "";
            String sHorizUp      = "";
            String sHorizDown    = "";
            int iStartDegree     = 0;
            int iEndDegree       = 0;
            int iStartVal        = 0;
            int iEndVal          = 0;
            boolean bIsClockwise = true;

            while( xmlEvent != XmlPullParser.END_DOCUMENT )
            {
                String sTagName = xmlParser.getName();

                // Switch XML Events
                switch ( xmlEvent )
                {
                    // Do nothing on Start Tags
                    case XmlPullParser.START_TAG:
                        break;

                    // Get the Parser Text Value
                    case XmlPullParser.TEXT:
                        sTagText = xmlParser.getText();
                        break;

                    // Set Values on End Tags
                    case XmlPullParser.END_TAG:
                        if( sTagName.equals( "estimoteObject" ) )
                        {
                            // Create the config for this object
                            ObjectConfig newConfig = new ObjectConfig();

                            switch( iCurrPattern )
                            {
                                case( Constants.TIMES_USED ):
                                    newConfig.configCounting( iTimeout );
                                    break;
                                case( Constants.OBJECT_STATE ):
                                    newConfig.configStates( sVertUp, sVertDown, sVertLeft, sVertRight, sHorizUp, sHorizDown );
                                    break;
                                case( Constants.OBJECT_ROTATION ):
                                    newConfig.configRotation( iStartDegree, iEndDegree, iStartVal, iEndVal, bIsClockwise );
                                    break;
                            }

                            objectList.add( new EstimoteObject( sCurrID, sCurrName, iCurrPattern, newConfig ) );
                        }
                        else if( sTagName.equals( "estimoteID" ) )
                        {
                            sCurrID = sTagText;
                        }
                        else if( sTagName.equals( "objectName" ) )
                        {
                            sCurrName = sTagText;
                        }
                        else if( sTagName.equals( "pattern" ) )
                        {
                            try
                            {
                                iCurrPattern = Integer.parseInt( sTagText );
                            }
                            catch( NumberFormatException e )
                            {
                                Log.e( "XML Writer", "Integer Reading Failure" );
                            }
                        }
                        else if( sTagName.equals("timeout") )
                        {
                            iTimeout = Integer.parseInt( sTagText );
                        }
                        else if( sTagName.equals("vertup") )
                        {
                            sVertUp = sTagText;
                        }
                        else if( sTagName.equals("vertdown") )
                        {
                            sVertDown = sTagText;
                        }
                        else if( sTagName.equals("vertleft") )
                        {
                            sVertLeft = sTagText;
                        }
                        else if( sTagName.equals("vertright") )
                        {
                            sVertRight = sTagText;
                        }
                        else if( sTagName.equals("horizup") )
                        {
                            sHorizUp = sTagText;
                        }
                        else if( sTagName.equals("horizdown") )
                        {
                            sHorizDown = sTagText;
                        }
                        else if( sTagName.equals("startdegree") )
                        {
                            iStartDegree = Integer.parseInt( sTagText );
                        }
                        else if( sTagName.equals("enddegree") )
                        {
                            iEndDegree = Integer.parseInt( sTagText );
                        }
                        else if( sTagName.equals("startval") )
                        {
                            iStartVal = Integer.parseInt( sTagText );
                        }
                        else if( sTagName.equals("endval") )
                        {
                            iEndVal = Integer.parseInt( sTagText );
                        }
                        else if( sTagName.equals("isclockwise") )
                        {
                            if( sTagText.equals( "true" ) )
                                bIsClockwise = true;
                            else
                                bIsClockwise = false;
                        }
                        break;
                }

                // Get the next event and loop
                xmlEvent = xmlParser.next();
            }
        }
        catch( Exception e )
        {
            // TODO - Handle Exception

            Log.e( "XML - Get All", e.toString() );
        }

        // Return the list
        return objectList;
    }


    // Create an entry for a new Estimote Object in the file
    public static void addObject( EstimoteObject newObject )
    {
        // Get the Object List
        List<EstimoteObject> allObjects = getAllObjectsInUse();

        // Add the new Object
        allObjects.add( newObject );

        // Write the new List
        writeObjects( allObjects );
    }


    // Edit an Estimote Object Entry
    public static void editObject( EstimoteObject oldObject , EstimoteObject newObject )
    {
        // Edit the Object List
        List<EstimoteObject> allObjects = getAllObjectsInUse();

        // Loop through objects and remove the correct one
        for( int iObject = 0; iObject < allObjects.size(); iObject++ )
        {
            // Compare ID's - Remove if equal
            if( allObjects.get( iObject ).getEstimoteID().equals( oldObject.getEstimoteID() ) )
                allObjects.remove( iObject );
        }

        // Add the new Object
        allObjects.add( newObject );

        // Write the new List
        writeObjects( allObjects );
    }


    // Remove a Estimote Object from the file (If it exists)
    public static void deleteObject( EstimoteObject oldObject )
    {
        // Edit the Object List
        List<EstimoteObject> allObjects = getAllObjectsInUse();

        // Loop through objects and remove the correct one
        for( int iObject = 0; iObject < allObjects.size(); iObject++ )
        {
            // Compare ID's - Remove if equal
            if( allObjects.get( iObject ).getEstimoteID().equals( oldObject.getEstimoteID() ) )
                allObjects.remove( iObject );
        }

        // Write the new List
        writeObjects( allObjects );
    }


    // Writes the given list of Estimote Objects to the XML File
    private static void writeObjects( List<EstimoteObject> objectsToWrite )
    {
        try
        {
            // Get Stream/Serializer/Writer
            FileOutputStream outputStream = new FileOutputStream( m_xmlFile );
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            // Setupt XML Writing
            xmlSerializer.setOutput( writer );
            xmlSerializer.startDocument( "UTF-8", true );


            for( int iObject = 0; iObject < objectsToWrite.size(); iObject++ )
            {
                // Write XML Details
                xmlSerializer.startTag( null, "estimoteObject" );
                xmlSerializer.startTag( null, "estimoteID" );
                xmlSerializer.text( objectsToWrite.get( iObject ).getEstimoteID() );
                xmlSerializer.endTag( null, "estimoteID" );
                xmlSerializer.startTag( null, "objectName" );
                xmlSerializer.text( objectsToWrite.get( iObject ).getObjectName() );
                xmlSerializer.endTag( null, "objectName" );
                xmlSerializer.startTag( null, "pattern" );
                xmlSerializer.text( Integer.toString( objectsToWrite.get( iObject ).getPattern() ) );
                xmlSerializer.endTag( null, "pattern" );
                xmlSerializer.startTag( null, "timeout" );
                xmlSerializer.text( Integer.toString( objectsToWrite.get( iObject ).getConfig().getTimeout() ) );
                xmlSerializer.endTag( null, "timeout" );
                xmlSerializer.startTag( null, "vertup" );
                xmlSerializer.text( objectsToWrite.get( iObject ).getConfig().getVerticalUpString() );
                xmlSerializer.endTag( null, "vertup" );
                xmlSerializer.startTag( null, "vertdown" );
                xmlSerializer.text( objectsToWrite.get( iObject ).getConfig().getVerticalDownString() );
                xmlSerializer.endTag( null, "vertdown" );
                xmlSerializer.startTag( null, "vertleft" );
                xmlSerializer.text( objectsToWrite.get( iObject ).getConfig().getVerticalLeftString() );
                xmlSerializer.endTag( null, "vertleft" );
                xmlSerializer.startTag( null, "vertright" );
                xmlSerializer.text( objectsToWrite.get( iObject ).getConfig().getVerticalRightString() );
                xmlSerializer.endTag( null, "vertright" );
                xmlSerializer.startTag( null, "horizup" );
                xmlSerializer.text( objectsToWrite.get( iObject ).getConfig().getHorizontalUpString() );
                xmlSerializer.endTag( null, "horizup" );
                xmlSerializer.startTag( null, "horizdown" );
                xmlSerializer.text( objectsToWrite.get( iObject ).getConfig().getHorizontalDownString() );
                xmlSerializer.endTag( null, "horizdown" );
                xmlSerializer.startTag( null, "startdegree" );
                xmlSerializer.text( Integer.toString( objectsToWrite.get( iObject ).getConfig().getStartDegree() ) );
                xmlSerializer.endTag( null, "startdegree" );
                xmlSerializer.startTag( null, "enddegree" );
                xmlSerializer.text( Integer.toString( objectsToWrite.get( iObject ).getConfig().getEndDegree() ) );
                xmlSerializer.endTag( null, "enddegree" );
                xmlSerializer.startTag( null, "startval" );
                xmlSerializer.text( Integer.toString( objectsToWrite.get( iObject ).getConfig().getStartValue() ) );
                xmlSerializer.endTag( null, "startval" );
                xmlSerializer.startTag( null, "endval" );
                xmlSerializer.text( Integer.toString( objectsToWrite.get( iObject ).getConfig().getEndValue() ) );
                xmlSerializer.endTag( null, "endval" );
                xmlSerializer.startTag( null, "isclockwise" );
                xmlSerializer.text( Boolean.toString( objectsToWrite.get( iObject ).getConfig().getIsClockwise() ) );
                xmlSerializer.endTag( null, "isclockwise" );
                xmlSerializer.endTag( null, "estimoteObject" );
                xmlSerializer.endDocument();
                xmlSerializer.flush();

                Log.d( "Write Rot", "sd = " + Integer.toString( objectsToWrite.get( iObject ).getConfig().getStartDegree() ) + ", ed = " + Integer.toString( objectsToWrite.get( iObject ).getConfig().getEndDegree() ) + ", sv = " + Integer.toString( objectsToWrite.get( iObject ).getConfig().getStartValue() ) + ", ev = " + Integer.toString( objectsToWrite.get( iObject ).getConfig().getEndValue() ) );
            }

            // Get the Data to Write
            String sDataToWrite = writer.toString();

            // Finally, write the bytes and close the stream
            outputStream.write( sDataToWrite.getBytes() );
            outputStream.close();
        }
        catch( Exception e )
        {
            Log.e( "XML Add New", e.toString() );
        }

        // The Object List has changed - Notify the Data-Recording Service
        m_Context.startService( new Intent( m_Context, EstimoteService.class ) );
    }
}
