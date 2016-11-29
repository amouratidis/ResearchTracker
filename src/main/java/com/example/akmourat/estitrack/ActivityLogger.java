package com.example.akmourat.estitrack;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by akmourat on 9/16/2016.
 */
public class ActivityLogger
{
        private final static String FILE        = "Logs/ActivityLog.csv";
        private final static String FOLDER      = "Logs";
        private final static String FILE_HEADER = "Timestamp,Class,Activity\n";

        private static FileOutputStream m_OutputStream;
        private static File m_LoggingFile;
        private static Context m_Context;

        private static boolean m_IsSetup = false;


        // Write a Nearable Packet to the log
        public static void writeToLog( String sClassName, String sActivityString )
        {
            if( m_IsSetup )
            {
                // Break out if we're not ready to write
                if( m_OutputStream == null )
                    return;

                // Attempt to write the line to the current file
                try
                {
                    m_OutputStream.write( ( System.currentTimeMillis() + "," + sClassName + "," + sActivityString ).getBytes() );
                }
                catch( IOException e )
                {
                    Log.e( "Logger Writing Error", e.toString() );
                }
            }
            else
            {
                // Do nothing until the logger is setup
            }
        }


        //
        public static void setupLogger( Context appContext ) throws FileNotFoundException
        {
            // If the Logger is already running - Do nothing
            if( m_IsSetup )
                return;

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

            // Set the boolean - Allow for recording
            m_IsSetup = true;
        }


        // Check if we're able to write to external storage
        private static boolean checkStorage()
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
