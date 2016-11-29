package com.example.akmourat.estitrack;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;

import java.util.List;

/**
 * Created by akmourat on 6/27/2016.
 */
public class EstimoteService extends Service
{
    Logger m_Logger;
    BeaconManager m_BeaconManager;


    @Override
    public int onStartCommand( Intent intent, int flags, int startId )
    {
        // Get the List of Objects in Use
        List<EstimoteObject> newObjectList = EstimoteDataManager.getAllObjectsInUse();

        // Update the Logger
        m_Logger.updateList( newObjectList );

        // Allows the Service to continue running until explicitly stopped
        return START_STICKY;
    }


    @Override
    public IBinder onBind( Intent intent )
    {
        // Don't allow this service to be bound
        return null;
    }


    @Override
    public void onCreate()
    {
        try
        {
            // Attempt to set up the Beacon Manager
            setupBeaconManager();

            // Attempt to set up the Logger
            m_Logger = new Logger( this.getApplicationContext() );
        }
        catch( Exception e )
        {
            Log.e( "Estimote Service", "Unable to start Service - " + e.toString() );
        }
    }


    @Override
    public void onDestroy()
    {
        // Clean-up the Beacon Manager
        m_BeaconManager.disconnect();

        // Clean-up the Logger
        m_Logger.closeFile();
    }


    // Used to Setup the Beacon Manager - Listens for nearby Stickers
    private void setupBeaconManager()
    {
        // Create the Beacon Manager
        m_BeaconManager = new BeaconManager( this.getApplicationContext() );

        // Setup the Scanning Time
        m_BeaconManager.setForegroundScanPeriod( 1000, 0 );

        // Add a Sticker/Nearable Listener
        m_BeaconManager.setNearableListener( new BeaconManager.NearableListener()
        {
            @Override public void onNearablesDiscovered( List stickers )
            {
                // Update the StickerList
                LastEstimoteList.setList( stickers );

                // Individually pass each Nearable to the logger
                for( int iSticker = 0; iSticker < stickers.size(); iSticker++ )
                    m_Logger.writeToLog( (Nearable) stickers.get( iSticker ) );
            }
        });

        // Finally, connect the Beacon Manager
        m_BeaconManager.connect(new BeaconManager.ServiceReadyCallback()
        {
            @Override public void onServiceReady()
            {
                // Nearable discovery.
                m_BeaconManager.startNearableDiscovery();
            }
        });
    }
}
