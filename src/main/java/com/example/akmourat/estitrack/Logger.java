package com.example.akmourat.estitrack;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import com.estimote.sdk.Nearable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akmourat on 5/30/2016.
 */
public class Logger
{
    private final String FILE        = "Logs/MasterLog.csv";
    private final String FOLDER      = "Logs";
    private final String FILE_HEADER = "Timestamp,Name,Signal,Orientation,InMotion,Duration,XAccel,YAccel,ZAccel,Temperature\n";

    private FileOutputStream m_OutputStream;
    private File m_LoggingFile;
    private Context m_Context;

    private List<EstimoteObject> m_ObjectsToRecord;


    // Create the logger
    Logger( Context appContext ) throws FileNotFoundException
    {
        // Set the Context
        m_Context = appContext;

        // Check that the folder exists
        File loggingFolder = new File( m_Context.getExternalFilesDir( null ) + FOLDER );

        // If the folder does not yet exist - Mkdirs
        if( !loggingFolder.exists() )
            loggingFolder.mkdirs();

        // Create the File
        m_LoggingFile = new File( m_Context.getExternalFilesDir( null ) + FILE );

        // Create the OutputStream
        m_OutputStream = new FileOutputStream( m_LoggingFile, true );

        // Create an empty list - this will be set when we have data to record
        m_ObjectsToRecord = new ArrayList<EstimoteObject>();
    }


    // Updates the list of Objects that is being recorded
    public void updateList( List<EstimoteObject> newObjectList )
    {
        m_ObjectsToRecord = newObjectList;
    }



    // Close the current output stream
    public void closeFile()
    {
        // Scan for the new file
        MediaScannerConnection.scanFile( m_Context, new String[] { m_LoggingFile.getAbsolutePath() }, null, null );

        // If the output stream is already null, do nothing
        if( m_OutputStream == null )
            return;

        // Close the Stream
        try
        {
            m_OutputStream.close();
        }
        catch( IOException e )
        {
            Log.d( "Logger", "Unable to Close Stream" );
        }
    }


    // Write a Nearable Packet to the log
    public void writeToLog( Nearable currPacket )
    {
        // First, check if we're recording this packet
        if( isRecording( currPacket ) )
        {
            // Format the packet into a single string
            String sLine = formatNearable(currPacket);

            // Write the line to the file
            write(sLine);
        }
    }


    // Internally used to write a formatted line to the log
    private void write( String sLine )
    {
        // Break out if we're not ready to write
        if( m_OutputStream == null )
            return;

        // Attempt to write the line to the current file
        try
        {
            m_OutputStream.write( sLine.getBytes() );
        }
        catch( IOException e )
        {
            Log.e( "Logger Writing Error", e.toString() );
        }
    }


    // Format a given Nearable Packet into a single csv string
    private String formatNearable( Nearable currPacket )
    {
        String sLine = System.currentTimeMillis() + ","
                + KeyList.getNameById( currPacket.identifier ) + ","
                + currPacket.rssi + ","
                + currPacket.orientation + ","
                + currPacket.isMoving + ","
                + currPacket.currentMotionStateDuration + ","
                + currPacket.xAcceleration + ","
                + currPacket.yAcceleration + ","
                + currPacket.zAcceleration + ","
                + currPacket.temperature + "\n";

        return sLine;
    }


    // Checks if the Logger is currently recording a given packet
    private boolean isRecording( Nearable currPacket )
    {
        // Loop through the list of recorded packets
        for( int iObject = 0; iObject < m_ObjectsToRecord.size(); iObject++ )
        {
            // Compare ID's - If equal, return true
            if( m_ObjectsToRecord.get( iObject ).getEstimoteID().equals( currPacket.identifier ) )
                return true;
        }

        // Fall through means it wasn't found
        return false;
    }


    // Check if we're able to write to external storage
    private boolean checkStorage()
    {
        // Get the Current Storage State
        String state = Environment.getExternalStorageState();

        // Make sure External storage exists
        if( Environment.MEDIA_MOUNTED.equals( state ) )
            return true;

        // If not - Return false
        return false;
    }
}