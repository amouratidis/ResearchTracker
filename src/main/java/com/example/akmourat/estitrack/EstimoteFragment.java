package com.example.akmourat.estitrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by akmourat on 8/8/2016.
 */
public class EstimoteFragment extends DialogFragment
{
    private AlertDialog.Builder m_DialogBuilder;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState )
    {
        // Build the Example Applications Fragment
        m_DialogBuilder = new AlertDialog.Builder(getActivity());
        m_DialogBuilder.setTitle("Estimote Sticker Tutorial");
        m_DialogBuilder.setMessage( "Ensure sticker has made good contact with surface and will not detach.\n\n"
                + "Check that sticker will not be damaged by surrounding environment\n\n"
                + "When using sticker to measure state changes, ensure that the side of the sticker pointing down changes during state changes." );
        m_DialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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
