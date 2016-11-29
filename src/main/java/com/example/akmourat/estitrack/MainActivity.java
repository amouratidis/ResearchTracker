package com.example.akmourat.estitrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Startup code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Data Manager
        EstimoteDataManager.setContext( getApplicationContext() );

        // Start the Data-Recording service
        startService( new Intent( this, EstimoteService.class ) );

        // Setup the Activity Logger
        try
        {
            ActivityLogger.setupLogger( this );
        }
        catch( Exception e )
        {
            Log.e( "Activity Logger", "Unable to setup logger" );
        }

        // Show the Estimote Tutorial
        //EstimoteFragment estFragment = new EstimoteFragment();
        //estFragment.show( getFragmentManager(), "Estimote Tutorial" );
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        // Used to check that the Android requirements are set
        SystemRequirementsChecker.checkWithDefaultDialogs( this );

        // Get the list of Objects In Use
        List<EstimoteObject> objectsInUse = EstimoteDataManager.getAllObjectsInUse();

        // Loop through Objects in Use - Retrieve names
        List<String> sEstimoteStickers = new ArrayList<String>();
        for( int iObject = 0; iObject < objectsInUse.size(); iObject++ )
            sEstimoteStickers.add( objectsInUse.get( iObject ).getObjectName() );

        // Setup the List Adapter
        ArrayAdapter<String> aAdapter = new ArrayAdapter<String>( this, R.layout.list_textview, sEstimoteStickers );

        // Setup the ListView
        ListView estimoteList = (ListView) findViewById( R.id.estimoteObjectList );
        estimoteList.setAdapter( aAdapter );

        // Set the Click Listener
        estimoteList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick( AdapterView<?> parent, View view, int position, long id )
            {
                // Record Activity
                ActivityLogger.writeToLog( "MainActivity","View Object at position " + position );

                // Setup and Start the new Activity
                Intent newIntent = new Intent( view.getContext(), DashboardActivity.class );
                newIntent.putExtra( "objectIndex", position );
                startActivity( newIntent );
            }
        });

        // Set the Long Click Listener
        estimoteList.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id )
            {
                // Record Activity
                ActivityLogger.writeToLog( "MainActivity","Edit Object at position " + position );

                // Setup and Start the new Activity
                Intent newIntent = new Intent( view.getContext(), EditActivity.class );
                newIntent.putExtra( "objectIndex", position );
                newIntent.putExtra( "isEditActivity", true );
                startActivity( newIntent );

                return true;
            }

        });
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        // Clean-up the Data-Recording service
        stopService( new Intent( this, EstimoteService.class ) );
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        // Record Activity
        ActivityLogger.writeToLog( "MainActivity", "Back Pressed" );
    }


    // Go to create new tab
    public void newObjectClicked( View view )
    {
        // Record Activity
        ActivityLogger.writeToLog( "MainActivity", "Create new Object" );

        // First - Check the number of Objects Created
        int iObjectCount    = EstimoteDataManager.getAllObjectsInUse().size();
        boolean bShowDialog = false;

        // If the User has no Objects - Show Examples Dialog
        if( iObjectCount == 0 )
            bShowDialog = true;

        // Setup and Start the new Activity
        Intent newIntent = new Intent( this, EditActivity.class );
        newIntent.putExtra( "isEditActivity", false );
        newIntent.putExtra( "showDialog", bShowDialog );
        startActivity( newIntent );
    }
}
