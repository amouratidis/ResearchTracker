package com.example.akmourat.estitrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by akmourat on 8/8/2016.
 */
public class ExamplePicturesFragment extends DialogFragment
{
    private AlertDialog.Builder m_DialogBuilder;
    private ImageView m_CurrPicture;

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        // Setup Picture Variables
        int iPictureType = getArguments().getInt( "iPattern", 0 );
        int iCurrPictureRes = R.drawable.drawable_wallet;

        // Prepare the Builder
        m_DialogBuilder = new AlertDialog.Builder( getActivity() );

        // Set the Picture based on Passed Variables
        switch( iPictureType )
        {
            case( Constants.MOTION_TRACKING ):
                iCurrPictureRes = R.drawable.drawable_wallet;
                m_DialogBuilder.setTitle( "Example Wallet Placement" );
                break;

            case( Constants.TIMES_USED ):
                iCurrPictureRes = R.drawable.drawable_door;
                m_DialogBuilder.setTitle( "Example Door Placement" );
                break;

            case( Constants.OBJECT_STATE ):
                iCurrPictureRes = R.drawable.drawable_dishwasher;
                m_DialogBuilder.setTitle( "Example Dishwasher Placement" );
                break;

            case( Constants.OBJECT_ROTATION ):
                iCurrPictureRes = R.drawable.drawable_dial;
                m_DialogBuilder.setTitle( "Example Dial Placement" );
                break;

            case( Constants.OBJECT_TEMPERATURE ):
                iCurrPictureRes = R.drawable.drawable_kettle;
                m_DialogBuilder.setTitle( "Example Kettle Placement" );
                break;

            default:
                iCurrPictureRes = R.drawable.drawable_wallet;
                m_DialogBuilder.setTitle( "Example Wallet Placement" );
                break;
        }

        // Set Picture
        m_CurrPicture = new ImageView( getActivity().getApplicationContext() );
        m_CurrPicture.setImageResource( iCurrPictureRes );

        // Build the Example Applications Fragment
        m_DialogBuilder.setPositiveButton( "Next", new DialogInterface.OnClickListener(){
            public void onClick( DialogInterface dialog, int id )
            {
                // User closed the dialog - Nothing to do here
            }
        });
        m_DialogBuilder.setView( m_CurrPicture );

        // Create the Dialog object and return it
        return m_DialogBuilder.create();
    }
}
