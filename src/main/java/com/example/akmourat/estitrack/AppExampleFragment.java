package com.example.akmourat.estitrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by akmourat on 8/2/2016.
 */
public class AppExampleFragment extends DialogFragment
{
    private AlertDialog.Builder m_DialogBuilder;


    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        // Build the Example Applications Fragment
        m_DialogBuilder = new AlertDialog.Builder( getActivity() );
        m_DialogBuilder.setTitle( "Example Sticker Applications" );
        m_DialogBuilder.setMessage( "Motion Tracking\n - Shows when the object is in motion, or not in motion\n\n"
                + "Times Used\n - Counts the number of times an object has been moved\n\n"
                + "Object State\n - Shows the Object state based on the orientation of the sticker\n\n"
                + "Object Rotation\n - Shows the current rotation of the object\n - Calculates a user-set value based on the current rotation\n\n"
                + "Object Temperature\n - Shows the current temperature of the object" );
        m_DialogBuilder.setPositiveButton( "Close", new DialogInterface.OnClickListener(){
            public void onClick( DialogInterface dialog, int id )
            {
                // User closed the dialog - Nothing to do here
            }
        });

        /* Negative button if ever needed
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id)
            {
                // User cancelled the dialog
            }
        }); */

        // Create the Dialog object and return it
        return m_DialogBuilder.create();
    }
}
