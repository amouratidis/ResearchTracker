package com.example.akmourat.estitrack;

/**
 * Created by akmourat on 6/25/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.estimote.sdk.Nearable;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity
{
    boolean m_IsEdit = false;
    boolean m_ShowDialog = false;
    boolean m_InitListener = true;
    EstimoteObject m_CurrObject;
    ArrayAdapter<EstimoteObject> m_EstimoteAdapter;
    ArrayAdapter<String> m_PatternAdapter;
    TabHost m_TabHost;
    TextView m_CurrRotationView;

    Handler rotationHandler = new Handler();
    Runnable updateRotation = new Runnable()
    {
        @Override
        public void run()
        {
            // Reset the Alpha of all images
            ImageView imageVertUp    = (ImageView) findViewById( R.id.vertUpPic );
            ImageView imageVertDown  = (ImageView) findViewById( R.id.vertDownPic );
            ImageView imageVertLeft  = (ImageView) findViewById( R.id.vertLeftPic );
            ImageView imageVertRight = (ImageView) findViewById( R.id.vertRightPic );
            ImageView imageHorizUp   = (ImageView) findViewById( R.id.horizUpPic );
            ImageView imageHorizDown = (ImageView) findViewById( R.id.horizDownPic );

            imageVertUp.setImageAlpha( 76 );
            imageVertDown.setImageAlpha( 76 );
            imageVertLeft.setImageAlpha( 76 );
            imageVertRight.setImageAlpha( 76 );
            imageHorizUp.setImageAlpha( 76 );
            imageHorizDown.setImageAlpha( 76 );

            // Get the objects
            Spinner idSpinner             = (Spinner) findViewById( R.id.spinnerStickerID );
            EstimoteObject selectedObject = (EstimoteObject) idSpinner.getSelectedItem();
            String sID                    = selectedObject.getEstimoteID();

            // Get the new data - if it exists
            List<Nearable> objectList = LastEstimoteList.getList();

            // Loop through the list, attempt to find the current Object
            for( int iNearable = 0; iNearable < objectList.size(); iNearable++ )
            {
                // Check if the current Nearable is the correct one
                if( objectList.get( iNearable ).identifier.equals( sID ) )
                {
                    // Update Sticker State
                    switch( objectList.get( iNearable ).orientation )
                    {
                        case VERTICAL:
                            imageVertUp.setImageAlpha( 255 );
                            break;
                        case VERTICAL_UPSIDE_DOWN:
                            imageVertDown.setImageAlpha( 255 );
                            break;
                        case LEFT_SIDE:
                            imageVertLeft.setImageAlpha( 255 );
                            break;
                        case RIGHT_SIDE:
                            imageVertRight.setImageAlpha( 255 );
                            break;
                        case HORIZONTAL:
                            imageHorizUp.setImageAlpha( 255 );
                            break;
                        case HORIZONTAL_UPSIDE_DOWN:
                            imageHorizDown.setImageAlpha( 255 );
                            break;
                    }

                    // Update Rotation Variable
                    double iRotation = 0;

                    // Get the Clockwise setting
                    CheckBox clockwiseBox = (CheckBox) findViewById( R.id.clockwiseCheck );

                    // Calculate the Rotation
                    if( clockwiseBox.isChecked() )
                        iRotation = Math.atan2( -objectList.get( iNearable ).xAcceleration, -objectList.get( iNearable ).yAcceleration ) * ( 180 / Math.PI );
                    else
                        iRotation = Math.atan2( objectList.get( iNearable ).xAcceleration, -objectList.get( iNearable ).yAcceleration ) * ( 180 / Math.PI );

                    // Format for 360degrees
                    if( iRotation < 0 )
                        iRotation = 180 + ( 180 + iRotation );

                    m_CurrRotationView.setText( "Current Rotation: " + (int)iRotation + "°" );
                }
            }

            // Run this again in one second
            rotationHandler.postDelayed( this, 1000 );
        }
    };


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        // Setup the Activity
        super.onCreate( savedInstanceState );
        setContentView( R.layout.edit_layout );

        // Grab the Bundle and set Activity Type
        Bundle bundleType = getIntent().getExtras();
        if ( bundleType != null )
        {
            m_IsEdit     = bundleType.getBoolean( "isEditActivity", false );
            m_ShowDialog = bundleType.getBoolean( "showDialog", false );
        }

        // Setup the Pattern Spinner
        Spinner patternSpinner = (Spinner) findViewById( R.id.spinnerPattern );
        m_PatternAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_dropdown_item_1line, Constants.PATTERNS );
        patternSpinner.setAdapter( m_PatternAdapter );

        // Setup Tabs
        m_TabHost = (TabHost) findViewById( R.id.patternTabHost );
        m_TabHost.setup();

        // Empty Tab
        TabHost.TabSpec emptySpec = m_TabHost.newTabSpec( "Empty Tab" );
        emptySpec.setContent( R.id.emptyTab );
        emptySpec.setIndicator( "Empty Tab" );
        m_TabHost.addTab( emptySpec );

        // Counting Tab
        TabHost.TabSpec countSpec = m_TabHost.newTabSpec( "Counting Tab" );
        countSpec.setContent( R.id.countingTab );
        countSpec.setIndicator( "Counting Tab" );
        m_TabHost.addTab( countSpec );

        // State Tab
        TabHost.TabSpec stateSpec = m_TabHost.newTabSpec( "State Tab" );
        stateSpec.setContent( R.id.stateTab );
        stateSpec.setIndicator( "State Tab" );
        m_TabHost.addTab( stateSpec );

        // Rotation Tab
        TabHost.TabSpec rotationSpec = m_TabHost.newTabSpec( "Rotation Tab" );
        rotationSpec.setContent( R.id.rotationTab );
        rotationSpec.setIndicator( "Rotation Tab" );
        m_TabHost.addTab( rotationSpec );


        ImageView imageVertUp    = (ImageView) findViewById( R.id.vertUpPic );
        ImageView imageVertDown  = (ImageView) findViewById( R.id.vertDownPic );

        // Setup Edit/New
        if( m_IsEdit )
            setupEdit();
        else
            setupNew();

        // Spinner on Click
        patternSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected( AdapterView<?> parentView, View selectedItemView, int position, long id )
            {
                // Record Activity
                ActivityLogger.writeToLog( "EditActivity", "Selecting Pattern " + position );

                // Ensure the Correct Tab is showing
                switch( position )
                {
                    case( Constants.TIMES_USED ):
                        m_TabHost.setCurrentTab( Constants.COUNT_TAB );
                        break;
                    case( Constants.OBJECT_STATE ):
                        m_TabHost.setCurrentTab( Constants.STATE_TAB );
                        break;
                    case( Constants.OBJECT_ROTATION ):
                        m_TabHost.setCurrentTab( Constants.ROTATION_TAB );
                        break;
                    default:
                        m_TabHost.setCurrentTab( Constants.EMPTY_TAB );
                        break;
                }

                if( !m_InitListener )
                {
                    // Show the Dialog
                    Bundle bundleArgs = new Bundle();
                    bundleArgs.putInt( "iPattern", position );

                    ExamplePicturesFragment exPicFragment = new ExamplePicturesFragment();
                    exPicFragment.setArguments( bundleArgs );
                    exPicFragment.show( getFragmentManager(), "Example Pictures" );
                }
                else
                {
                    m_InitListener = false;
                }
            }

            @Override
            public void onNothingSelected( AdapterView<?> parentView )
            {
                // Nothing to do here
            }

        });

        // Show the Example Applications
        AppExampleFragment exFragment = new AppExampleFragment();
        exFragment.show( getFragmentManager(), "Example Applications" );

        // Setup the Rotation Runnable
        m_CurrRotationView = (TextView) findViewById( R.id.textCurrentRotation );
        rotationHandler.postDelayed( updateRotation, 1000 );
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        // Record Activity
        ActivityLogger.writeToLog( "EditActivity", "Back Pressed" );
    }


    // Called on Creation if editing a selected object
    private void setupEdit()
    {
        // Setup the Object Index that is selected
        int iObjectIndex = -1;

        // Grab the Bundle and set Activity Type
        Bundle bundleType = getIntent().getExtras();
        if ( bundleType != null )
            iObjectIndex = bundleType.getInt( "objectIndex", -1 );

        // Load the Estimote Object
        m_CurrObject = EstimoteDataManager.getObject( iObjectIndex );

        // Create a list with the Estimote Object
        List<EstimoteObject> spinnerObjects = new ArrayList<>();
        spinnerObjects.add( m_CurrObject );

        // Get the objects
        Spinner idSpinner   = (Spinner) findViewById( R.id.spinnerStickerID );
        EditText editName   = (EditText) findViewById( R.id.editStickerName );
        Spinner patternSpin = (Spinner) findViewById( R.id.spinnerPattern );

        // Setup the Adapter
        m_EstimoteAdapter = new ArrayAdapter<EstimoteObject>( this, android.R.layout.simple_dropdown_item_1line, spinnerObjects );
        idSpinner.setAdapter( m_EstimoteAdapter );

        // Set fields
        idSpinner.setClickable( false );
        idSpinner.setSelection( m_EstimoteAdapter.getPosition( m_CurrObject ) );
        editName.setText( m_CurrObject.getObjectName() );

        switch( m_CurrObject.getPattern() )
        {
            case( Constants.TIMES_USED ):
                EditText timeoutEdit = (EditText) findViewById( R.id.timeoutEdit );
                timeoutEdit.setText( String.valueOf( m_CurrObject.getConfig().getTimeout() ) );
                break;
            case( Constants.OBJECT_STATE ):
                EditText vertUpEdit    = (EditText) findViewById( R.id.vertUpEdit );
                EditText vertDownEdit  = (EditText) findViewById( R.id.vertDownEdit );
                EditText vertLeftEdit  = (EditText) findViewById( R.id.vertLeftEdit );
                EditText vertRightEdit = (EditText) findViewById( R.id.vertRightEdit );
                EditText horizUpEdit   = (EditText) findViewById( R.id.horizUpEdit );
                EditText horizDownEdit = (EditText) findViewById( R.id.horizDownEdit );
                vertUpEdit.setText( m_CurrObject.getConfig().getVerticalUpString() );
                vertDownEdit.setText( m_CurrObject.getConfig().getVerticalDownString() );
                vertLeftEdit.setText( m_CurrObject.getConfig().getVerticalLeftString() );
                vertRightEdit.setText( m_CurrObject.getConfig().getVerticalRightString() );
                horizUpEdit.setText( m_CurrObject.getConfig().getHorizontalUpString() );
                horizDownEdit.setText( m_CurrObject.getConfig().getHorizontalDownString() );
                break;
            case( Constants.OBJECT_ROTATION ):
                EditText startDegreeEdit = (EditText) findViewById( R.id.startDegreeEdit );
                EditText endDegreeEdit   = (EditText) findViewById( R.id.endDegreeEdit );
                EditText startValEdit    = (EditText) findViewById( R.id.startValueEdit );
                EditText endValEdit      = (EditText) findViewById( R.id.endValueEdit );
                CheckBox clockwiseCheck  = (CheckBox) findViewById( R.id.clockwiseCheck );
                startDegreeEdit.setText(  String.valueOf( m_CurrObject.getConfig().getStartDegree() ) );
                endDegreeEdit.setText(  String.valueOf( m_CurrObject.getConfig().getEndDegree() ) );
                startValEdit.setText(  String.valueOf( m_CurrObject.getConfig().getStartValue() ) );
                endValEdit.setText( String.valueOf( m_CurrObject.getConfig().getEndValue() ) );
                clockwiseCheck.setChecked( m_CurrObject.getConfig().getIsClockwise() );
                break;
        }

        // Select the Correct Pattern
        patternSpin.setSelection( m_CurrObject.getPattern() );
    }


    // Called on Creation if making a New Object
    private void setupNew()
    {
        // Setup the Spinner
        Spinner idSpinner                   = (Spinner) findViewById( R.id.spinnerStickerID );
        List<EstimoteObject> spinnerObjects = KeyList.getUseableObjects();

        // Setup the Adapter
        m_EstimoteAdapter = new ArrayAdapter<EstimoteObject>( this, android.R.layout.simple_dropdown_item_1line, spinnerObjects );
        idSpinner.setAdapter( m_EstimoteAdapter );

        // Disable delete button
        Button deleteButton = (Button) findViewById( R.id.buttonDelete );
        deleteButton.setEnabled( false );
    }


    // Called when the done button is clicked
    public void doneClicked( View view )
    {
        // Record Activity
        ActivityLogger.writeToLog( "EditActivity", "Done Button Clicked" );

        // Get the objects
        Spinner idSpinner   = (Spinner) findViewById( R.id.spinnerStickerID );
        EditText editName   = (EditText) findViewById( R.id.editStickerName );
        Spinner patternSpin = (Spinner) findViewById( R.id.spinnerPattern );

        // Convert objects to usable fields
        EstimoteObject selectedObject = (EstimoteObject) idSpinner.getSelectedItem();
        String sID   = selectedObject.getEstimoteID();
        String sName = editName.getText().toString();
        int iPattern = patternSpin.getSelectedItemPosition();

        // Create the Object Config
        ObjectConfig newConfig = new ObjectConfig();
        switch( iPattern )
        {
            case( Constants.TIMES_USED ):
                EditText timeoutEdit = (EditText) findViewById( R.id.timeoutEdit );
                newConfig.configCounting( Integer.parseInt( timeoutEdit.getText().toString() ) );
                break;
            case( Constants.OBJECT_STATE ):
                EditText vertUpEdit    = (EditText) findViewById( R.id.vertUpEdit );
                EditText vertDownEdit  = (EditText) findViewById( R.id.vertDownEdit );
                EditText vertLeftEdit  = (EditText) findViewById( R.id.vertLeftEdit );
                EditText vertRightEdit = (EditText) findViewById( R.id.vertRightEdit );
                EditText horizUpEdit   = (EditText) findViewById( R.id.horizUpEdit );
                EditText horizDownEdit = (EditText) findViewById( R.id.horizDownEdit );
                newConfig.configStates( vertUpEdit.getText().toString(), vertDownEdit.getText().toString(), vertLeftEdit.getText().toString(), vertRightEdit.getText().toString(), horizUpEdit.getText().toString(), horizDownEdit.getText().toString() );
                break;
            case( Constants.OBJECT_ROTATION ):
                EditText startDegreeEdit = (EditText) findViewById( R.id.startDegreeEdit );
                EditText endDegreeEdit   = (EditText) findViewById( R.id.endDegreeEdit );
                EditText startValEdit    = (EditText) findViewById( R.id.startValueEdit );
                EditText endValEdit      = (EditText) findViewById( R.id.endValueEdit );
                CheckBox clockwiseCheck  = (CheckBox) findViewById( R.id.clockwiseCheck );
                newConfig.configRotation( Integer.parseInt( startDegreeEdit.getText().toString() ), Integer.parseInt( endDegreeEdit.getText().toString() ), Integer.parseInt( startValEdit.getText().toString() ), Integer.parseInt( endValEdit.getText().toString() ), clockwiseCheck.isChecked() );
                break;
        }

        // Create the new Estimote Object
        EstimoteObject newObject = new EstimoteObject( sID, sName, iPattern, newConfig );

        // Add the object to the XML
        if( m_IsEdit )
            EstimoteDataManager.editObject(m_CurrObject, newObject);
        else
            EstimoteDataManager.addObject( newObject );

        // Get the index of the new object
        int iIndex = EstimoteDataManager.getIndex( newObject );

        // Start the Dashboard Activity
        Intent newIntent = new Intent( view.getContext(), DashboardActivity.class );
        newIntent.putExtra( "objectIndex", iIndex );
        startActivity( newIntent );

        // End the Activity
        this.finish();
    }


    // Called when the delete button is clicked
    public void deleteClicked( View view )
    {
        // Record Activity
        ActivityLogger.writeToLog( "EditActivity", "Delete Button Clicked" );

        // Get the Spinner
        Spinner idSpinner = (Spinner) findViewById( R.id.spinnerStickerID );

        // Get selected object
        EstimoteObject selectedObject = (EstimoteObject) idSpinner.getSelectedItem();

        EstimoteDataManager.deleteObject( selectedObject );

        // End the Activity
        this.finish();
    }
}

