package com.example.akmourat.estitrack;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estimote.sdk.Nearable;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akmourat on 7/5/2016.
 */
public class DashboardActivity extends AppCompatActivity
{
    // Constants
    private final int NUM_DATAPOINTS = 60;

    // Pattern Variable
    private int m_Pattern;

    // Text Variables
    TextView m_DetectedTracker;
    TextView m_PatternTracker;

    // Graph Variables
    GraphView m_XAccelGraph;
    GraphView m_YAccelGraph;
    GraphView m_ZAccelGraph;
    GraphView m_TempGraph;
    GraphView m_SignalGraph;

    // Datapoint Variables
    List<Double> m_XAccelData = new ArrayList<Double>();
    List<Double> m_YAccelData = new ArrayList<Double>();
    List<Double> m_ZAccelData = new ArrayList<Double>();
    List<Double> m_TempData   = new ArrayList<Double>();
    List<Double> m_SignalData = new ArrayList<Double>();

    // Counter Variables
    private int m_CurrCount;
    private boolean m_LastMotion;
    private long m_Timeout = 0;

    // User Configuration
    private ObjectConfig m_Config;

    private String m_ObjectID = "";

    Handler graphHandler = new Handler();
    Runnable updateGraphs = new Runnable()
    {
        @Override
        public void run()
        {
            // Boolean found Sticker
            boolean bFound = false;

            // Get the new data - if it exists
            List<Nearable> objectList = LastEstimoteList.getList();

            // Loop through the list, attempt to find the current Object
            for( int iNearable = 0; iNearable < objectList.size(); iNearable++ )
            {
                // Check if the current Nearable is the correct one
                if( objectList.get( iNearable ).identifier.equals( m_ObjectID ) )
                {
                    updateGraphs(objectList.get(iNearable));

                    bFound = true;
                }
            }

            // Show Found result
            if( bFound )
                m_DetectedTracker.setText( Constants.DETECTED );
            else
                m_DetectedTracker.setText( Constants.UNDETECTED );

            // Run this again in one second
            graphHandler.postDelayed( this, 1000 );
        }
    };


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        // Setup the Activity
        super.onCreate( savedInstanceState );
        setContentView( R.layout.dashboard_layout );

        // Graph Setup
        setupGraphs();

        // Setup Trackers
        m_DetectedTracker = (TextView) findViewById( R.id.detectedValue );
        m_PatternTracker  = (TextView) findViewById( R.id.patternValue );

        // Grab the Bundle and set Object Index
        Bundle bundleType = getIntent().getExtras();
        if ( bundleType != null )
        {
            int iObjectIndex = bundleType.getInt( "objectIndex", -1 );

            // Make sure we received an object ID
            if( iObjectIndex >= 0 )
            {
                // Get the Current Object
                EstimoteObject currObject = EstimoteDataManager.getObject( iObjectIndex );

                // Set the Config
                m_Config = currObject.getConfig();

                // Set the Object ID
                m_ObjectID = currObject.getEstimoteID();

                // Get the Pattern Index
                m_Pattern = currObject.getPattern();
                showPattern( m_Pattern );
            }
        }

        // Begin updating the graphs if an ID was received
        if( !m_ObjectID.isEmpty() )
            graphHandler.postDelayed( updateGraphs, 1000 );
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        // Record Activity
        ActivityLogger.writeToLog( "DashboardActivity", "Back Pressed" );
    }


    // Used to intit the Graph objects
    private void setupGraphs()
    {
        // Find all the Graphs
        m_XAccelGraph = (GraphView) findViewById( R.id.graphXAccel );
        m_YAccelGraph = (GraphView) findViewById( R.id.graphYAccel );
        m_ZAccelGraph = (GraphView) findViewById( R.id.graphZAccel );
        m_TempGraph   = (GraphView) findViewById( R.id.graphTemp );
        m_SignalGraph = (GraphView) findViewById( R.id.graphSignal );

        // Attach Series
        m_XAccelGraph.addSeries( new LineGraphSeries( m_XAccelData.toArray( new DataPoint[0] ) ) );
        m_YAccelGraph.addSeries( new LineGraphSeries( m_YAccelData.toArray( new DataPoint[0] ) ) );
        m_ZAccelGraph.addSeries( new LineGraphSeries( m_ZAccelData.toArray( new DataPoint[0] ) ) );
        m_TempGraph.addSeries(   new LineGraphSeries( m_TempData.toArray(   new DataPoint[0] ) ) );
        m_SignalGraph.addSeries( new LineGraphSeries( m_SignalData.toArray( new DataPoint[0] ) ) );

        // Set X-Accel Viewport
        m_XAccelGraph.getViewport().setXAxisBoundsManual( true );
        m_XAccelGraph.getViewport().setMinX( 0 );
        m_XAccelGraph.getViewport().setMaxX( NUM_DATAPOINTS );
        m_XAccelGraph.getViewport().setYAxisBoundsManual( true );
        m_XAccelGraph.getViewport().setMinY( -2000 );
        m_XAccelGraph.getViewport().setMaxY( 2000 );
        m_XAccelGraph.getGridLabelRenderer().setHorizontalLabelsVisible( false );
        m_XAccelGraph.getGridLabelRenderer().setHorizontalAxisTitle( "Time" );

        // Set X-Accel Viewport
        m_YAccelGraph.getViewport().setXAxisBoundsManual( true );
        m_YAccelGraph.getViewport().setMinX( 0 );
        m_YAccelGraph.getViewport().setMaxX( NUM_DATAPOINTS );
        m_YAccelGraph.getViewport().setYAxisBoundsManual( true );
        m_YAccelGraph.getViewport().setMinY( -2000 );
        m_YAccelGraph.getViewport().setMaxY( 2000 );
        m_YAccelGraph.getGridLabelRenderer().setHorizontalLabelsVisible( false );
        m_YAccelGraph.getGridLabelRenderer().setHorizontalAxisTitle( "Time" );

        // Set X-Accel Viewport
        m_ZAccelGraph.getViewport().setXAxisBoundsManual( true );
        m_ZAccelGraph.getViewport().setMinX( 0 );
        m_ZAccelGraph.getViewport().setMaxX( NUM_DATAPOINTS );
        m_ZAccelGraph.getViewport().setYAxisBoundsManual( true );
        m_ZAccelGraph.getViewport().setMinY( -2000 );
        m_ZAccelGraph.getViewport().setMaxY( 2000 );
        m_ZAccelGraph.getGridLabelRenderer().setHorizontalLabelsVisible( false );
        m_ZAccelGraph.getGridLabelRenderer().setHorizontalAxisTitle( "Time" );

        // Set Temp Viewport
        m_TempGraph.getViewport().setXAxisBoundsManual( true );
        m_TempGraph.getViewport().setMinX( 0 );
        m_TempGraph.getViewport().setMaxX( NUM_DATAPOINTS );
        m_TempGraph.getViewport().setYAxisBoundsManual( true );
        m_TempGraph.getViewport().setMinY( -100 );
        m_TempGraph.getViewport().setMaxY( 100 );
        m_TempGraph.getGridLabelRenderer().setHorizontalLabelsVisible( false );
        m_TempGraph.getGridLabelRenderer().setHorizontalAxisTitle( "Time" );

        // Set Signal Viewport
        m_SignalGraph.getViewport().setXAxisBoundsManual( true );
        m_SignalGraph.getViewport().setMinX( 0 );
        m_SignalGraph.getViewport().setMaxX( NUM_DATAPOINTS );
        m_SignalGraph.getViewport().setYAxisBoundsManual( true );
        m_SignalGraph.getViewport().setMinY( -100 );
        m_SignalGraph.getViewport().setMaxY( 0 );
        m_SignalGraph.getGridLabelRenderer().setHorizontalLabelsVisible( false );
        m_SignalGraph.getGridLabelRenderer().setHorizontalAxisTitle( "Time" );
    }


    // Show the Correct Graphs for each Pattern
    private void showPattern( int iPattern )
    {
        // Find the Titles for each Graph
        TextView xAccelText   = (TextView) findViewById( R.id.graphXAccelTitle );
        TextView yAccelText   = (TextView) findViewById( R.id.graphYAccelTitle );
        TextView zAccelText   = (TextView) findViewById( R.id.graphZAccelTitle );
        TextView tempText     = (TextView) findViewById( R.id.graphTempTitle );
        TextView signalText   = (TextView) findViewById( R.id.graphSignalTitle );

        // Get the Layout Perameters
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );

        switch( iPattern )
        {
            // Show Motion Tracking specific Graphs
            case Constants.MOTION_TRACKING:
            {
                m_PatternTracker.setText( "In Motion: " );

                ((ViewGroup) tempText.getParent()).removeView( tempText );
                ((ViewGroup) m_TempGraph.getParent()).removeView(m_TempGraph );

                layoutParams.addRule( RelativeLayout.BELOW, R.id.graphZAccel );
                signalText.setLayoutParams( layoutParams );

                break;
            }
            // Show Times Used specific Graphs
            case Constants.TIMES_USED:
            {
                m_PatternTracker.setText( "Count: " );

                ((ViewGroup) tempText.getParent()).removeView( tempText );
                ((ViewGroup) m_TempGraph.getParent()).removeView( m_TempGraph );

                layoutParams.addRule( RelativeLayout.BELOW, R.id.graphZAccel );
                signalText.setLayoutParams( layoutParams );

                break;
            }
            // Show Object State specific Graphs
            case Constants.OBJECT_STATE:
            {
                m_PatternTracker.setText( "State: " );

                ((ViewGroup) tempText.getParent()).removeView( tempText );
                ((ViewGroup) m_TempGraph.getParent()).removeView( m_TempGraph );

                layoutParams.addRule( RelativeLayout.BELOW, R.id.graphZAccel );
                signalText.setLayoutParams( layoutParams );

                break;
            }
            // Show Object Rotation specific Graphs
            case Constants.OBJECT_ROTATION:
            {
                m_PatternTracker.setText( "Rotation: " );

                ((ViewGroup) tempText.getParent()).removeView( tempText );
                ((ViewGroup) m_TempGraph.getParent()).removeView( m_TempGraph );

                layoutParams.addRule( RelativeLayout.BELOW, R.id.graphZAccel );
                signalText.setLayoutParams( layoutParams );

                break;
            }
            // Show Object Temperature specific Graphs
            case Constants.OBJECT_TEMPERATURE:
            {
                m_PatternTracker.setText( "Temperature: " );

                ((ViewGroup) xAccelText.getParent()).removeView( xAccelText );
                ((ViewGroup) m_XAccelGraph.getParent()).removeView( m_XAccelGraph );
                ((ViewGroup) yAccelText.getParent()).removeView( yAccelText );
                ((ViewGroup) m_YAccelGraph.getParent()).removeView( m_YAccelGraph );
                ((ViewGroup) zAccelText.getParent()).removeView( zAccelText );
                ((ViewGroup) m_ZAccelGraph.getParent()).removeView( m_ZAccelGraph );

                layoutParams.addRule( RelativeLayout.BELOW, R.id.patternValue );
                tempText.setLayoutParams( layoutParams );

                break;
            }
        }
    }


    // Add a given value to a given graph
    private void addToGraph( GraphView graphToUpdate, List<Double> yValues, double iNextValue )
    {
        // Exit safely if Graph has been removed
        if( graphToUpdate == null )
            return;

        // Remove oldest data point if too large
        if( yValues.size() >= NUM_DATAPOINTS )
            yValues.remove( 0 );

        // Add the new Data
        yValues.add( iNextValue );
        DataPoint[] dataPoints = new DataPoint[yValues.size()];

        // Fill the DataPoint Array
        for( int iDataPoint = 0; iDataPoint < yValues.size(); iDataPoint++ )
            dataPoints[iDataPoint] = new DataPoint( iDataPoint, yValues.get( iDataPoint ) );

        // Get the Series to Update
        LineGraphSeries<DataPoint> currSeries = (LineGraphSeries<DataPoint>) graphToUpdate.getSeries().get( 0 );

        // Update the Graph Data
        currSeries.resetData( dataPoints );
    }


    // Update the Pattern TextBox
    private void updateText( Nearable newNearable )
    {
        switch( m_Pattern )
        {
            case( Constants.MOTION_TRACKING ):
            {
                // Update Motion
                updateMotion( newNearable.isMoving );

                break;
            }

            case( Constants.TIMES_USED ):
            {
                // Update Counter
                updateCounter( newNearable.isMoving );

                break;
            }

            case( Constants.OBJECT_STATE ):
            {
                // Update State
                updateState( newNearable.orientation );

                break;
            }

            case( Constants.OBJECT_ROTATION ):
            {
                // Update Rotation
                updateRotation( newNearable.xAcceleration, newNearable.yAcceleration, newNearable.zAcceleration );

                break;
            }

            case( Constants.OBJECT_TEMPERATURE ):
            {
                // Update Temperature
                updateTemp( newNearable.temperature );

                break;
            }
        }
    }


    // Update each graph individually as needed
    private void updateGraphs( Nearable newNearable )
    {
        // Update the Text aswell
        updateText( newNearable );

        // Update the X-Acceleration Graph
        addToGraph( m_XAccelGraph, m_XAccelData, newNearable.xAcceleration );

        // Update the Y-Acceleration Graph
        addToGraph( m_YAccelGraph, m_YAccelData, newNearable.yAcceleration );

        // Update the Z-Acceleration Graph
        addToGraph( m_ZAccelGraph, m_ZAccelData, newNearable.zAcceleration );

        // Update the Temperature Graph
        addToGraph( m_TempGraph, m_TempData, newNearable.temperature );

        // Update the Signal Graph
        addToGraph( m_SignalGraph, m_SignalData, newNearable.rssi );
    }


    // Update the text of the Motion TextView
    private void updateMotion( boolean bInMotion )
    {
        // Do nothing if we don't have a motion tracker
        if( m_PatternTracker != null)
        {
            // Set the text based on the boolean value
            if( bInMotion )
                m_PatternTracker.setText( "In Motion: " + "True" );
            else
                m_PatternTracker.setText( "In Motion: " + "False" );
        }
    }


    // Update Counter
    private void updateCounter( boolean bIsMoving )
    {
        // Get the Current Time
        long lCurrTime = System.currentTimeMillis();

        if( bIsMoving && ( ( lCurrTime - m_Timeout ) >= ( m_Config.getTimeout() * 1000 ) ) )
        {
            // Update Counter and Show the new Value
            m_CurrCount++;
            m_PatternTracker.setText( "Count: " + m_CurrCount );

            // Update the new timeout with the current time
            m_Timeout = lCurrTime;
        }
    }


    // Update State
    private void updateState( Nearable.Orientation currOrientation )
    {
        // Make sure we have the ref
        if( m_PatternTracker != null )
        {
            String sStateString = "Undefined";

            switch( currOrientation )
            {
                case VERTICAL:
                {
                    sStateString = m_Config.getVerticalUpString();

                    break;
                }
                case VERTICAL_UPSIDE_DOWN:
                {
                    sStateString = m_Config.getVerticalDownString();

                    break;
                }
                case LEFT_SIDE:
                {
                    sStateString = m_Config.getVerticalLeftString();

                    break;
                }
                case RIGHT_SIDE:
                {
                    sStateString = m_Config.getVerticalRightString();

                    break;
                }
                case HORIZONTAL:
                {
                    sStateString = m_Config.getHorizontalUpString();

                    break;
                }
                case HORIZONTAL_UPSIDE_DOWN:
                {
                    sStateString = m_Config.getHorizontalDownString();

                    break;
                }
            }

            m_PatternTracker.setText( "State: " + sStateString );
        }
    }


    // Update Rotation
    private void updateRotation( double xVal, double yVal, double zVal )
    {
        // Make sure we have the ref
        if( m_PatternTracker != null )
        {
            double iRotation = 0;

            // Calculate the Rotation
            if( m_Config.getIsClockwise() )
                iRotation = Math.atan2( -xVal, -yVal ) * ( 180 / Math.PI );
            else
                iRotation = Math.atan2( xVal, -yVal ) * ( 180 / Math.PI );

            double iConfigStart = m_Config.getStartDegree();
            double iConfigEnd   = m_Config.getEndDegree();

            if( iConfigStart > 180 )
                iConfigStart = ( iConfigStart - 180 ) - 180;
            if( iConfigEnd > 180 )
                iConfigEnd = ( iConfigEnd - 180 ) - 180;

            double iValue = ( ( iRotation - iConfigStart ) / ( iConfigEnd - iConfigStart ) ) * ( m_Config.getEndValue() - m_Config.getStartValue() );

            // Format for 360degrees
            if( iRotation < 0 )
                iRotation = 180 + ( 180 + iRotation );

            m_PatternTracker.setText( "Rotation: " + String.format( "%.0f", iRotation ) + "°\nValue: " + String.format( "%.0f", iValue ) );
        }
    }


    // Update Temperature Tracker
    private void updateTemp( double dTemp )
    {
        // Make sure we have the ref
        if( m_PatternTracker != null )
        {
            m_PatternTracker.setText( "Temperature: " + String.format( "%.0f", dTemp ) + "°C" );
        }
    }
}
