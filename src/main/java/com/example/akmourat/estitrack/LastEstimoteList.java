package com.example.akmourat.estitrack;

import com.estimote.sdk.Nearable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by akmourat on 7/5/2016.
 */
public class LastEstimoteList
{
    private static Semaphore m_Semaphore = new Semaphore( 1 );
    private static List<Nearable> m_DataList = new ArrayList<Nearable>();


    // Used to safely acquire the last List of Nearable Objects
    public static List<Nearable> getList()
    {
        List<Nearable> returnList = new ArrayList<Nearable>();
        try
        {
            // Acquire access to the list
            m_Semaphore.acquire();

            // Get the list to return
            returnList = m_DataList;

            // Release access to the list
            m_Semaphore.release();
        }
        catch( Exception e )
        {
        }

        return returnList;
    }


    // Used to safely update the last List of Nearable Objects
    public static void setList( List<Nearable> newList )
    {
        try
        {
            // Acquire access to the list
            m_Semaphore.acquire();

            // Update the list
            m_DataList = newList;

            // Release access to the list
            m_Semaphore.release();
        }
        catch( Exception e )
        {
        }
    }
}
