package com.example.akmourat.estitrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akmourat on 5/30/2016.
 */
public class KeyList
{

    // List of Estimote Sticker Names and Numbers
    private static final String[] sEstimoteNames =
            {
                    "Shoe1",
                    "Fridge1",
                    "Chair1",
                    "Door1",
                    "Dog1",
                    "Bike1",
                    "Shoe2",
                    "Generic2",
                    "Fridge2",
                    "Bed2",
                    "Chair2",
                    "Door2",
                    "Car2",
                    "Bag2",
                    "Dog2",
                    "Bike2",
                    "Shoe3",
                    "Generic3",
                    "Fridge3",
                    "Bed3",
                    "Chair3",
                    "Door3",
                    "Car3",
                    "Bag3",
                    "Dog3",
                    "Bike3",
                    "Shoe4",
                    "Generic4",
                    "Fridge4",
                    "Bed4",
                    "Chair4",
                    "Door4",
                    "Car4",
                    "Bag4",
                    "Dog4",
                    "Bike4",
                    "Shoe5",
                    "Generic5",
                    "Fridge5",
                    "Bed5",
                    "Chair5",
                    "Door5",
                    "Car5",
                    "Bag5",
                    "Dog5",
                    "Bike5"
            };


    // List of Estimote Sticker Id's
    private static final String[] sEstimoteValues =
            {
                    "8f6028139db195c1",
                    "03696daac12f6b7b",
                    "62c4f9a0193c2d99",
                    "779529f813faed72",
                    "536b603efc922303",
                    "657f3254ae74eac5",
                    "554c8850c7bc9042",
                    "67046ed41908b118",
                    "715d0ef24717bee2",
                    "61418bec42055e83",
                    "a4bda8917a87f206",
                    "a4502c4ad1ce6f8c",
                    "33e74e51db04705f",
                    "669569371c5a6cfb",
                    "201b7f1653634062",
                    "7247b71f110ce9ec",
                    "bfc7dba50adb7b0a",
                    "46729d82f204dfd7",
                    "33429bcfacdf18dd",
                    "114e62602c23010c",
                    "d9e8d7ddf1ed92d8",
                    "61c3e46224897a54",
                    "3894868dcccecd5a",
                    "405dfc2bd6d887af",
                    "5772141b2e1c0673",
                    "84fefc255c30ea4c",
                    "fecfc03b2e656be2",
                    "e32a5c3697964201",
                    "1e768bd9e618bd2a",
                    "8e5d0a6813c04aed",
                    "e71f32058f498ef4",
                    "d36e0c3b15e026c3",
                    "b0dc66d4d09b47f2",
                    "43d4594d135b3b0a",
                    "802551fab97cddd8",
                    "a1aa1cfd9fdfb767",
                    "df26b606c8df9edb",
                    "91d06fb32562a3b5",
                    "5504bf6b58a3128b",
                    "51a509ef85a9e72b",
                    "d181e0d37579d36f",
                    "ede793099774d2e6",
                    "b72854768df0e7f1",
                    "93388be06ba3693c",
                    "5b8cdaa6f4c6f64e",
                    "0e1911b9e044d7c4"
            };


    public static String getNameById( String sId )
    {
        // Set the name to the Id - If the name is not found we'll just return the ID
        String sName = sId;

        // Loop through the Id list to find the index
        for( int iIdIndex = 0; iIdIndex < sEstimoteValues.length; iIdIndex++ )
        {
            // Compare the Id's
            if( sId.compareTo( sEstimoteValues[iIdIndex] ) == 0 )
            {
                // If the Id is found - Set the new name and break
                sName = sEstimoteNames[iIdIndex];

                break;
            }
        }

        // Return the Name found
        return sName;
    }


    public static List<EstimoteObject> getUseableObjects()
    {
        List<EstimoteObject> objectsInUse = EstimoteDataManager.getAllObjectsInUse();
        List<EstimoteObject> allObjects   = getAllObjects();

        // Loop through all objects in use - remove them from the all objects list
        for( int iObjectInUse = 0; iObjectInUse < objectsInUse.size(); iObjectInUse++ )
        {
            for( int iAllObjects = 0; iAllObjects < allObjects.size(); iAllObjects++ )
            {
                // Compare Object IDs - Remove if equal
                if( objectsInUse.get( iObjectInUse ).getEstimoteID().equals( allObjects.get( iAllObjects ).getEstimoteID() ) )
                    allObjects.remove( iAllObjects );
            }
        }

        return allObjects;
    }



    public static List<EstimoteObject> getAllObjects()
    {
        // Create an empty list
        List<EstimoteObject> allObjects = new ArrayList<EstimoteObject>();

        // Loop through internal array and fill list
        for( int iObjIndex = 0; iObjIndex < sEstimoteValues.length; iObjIndex++ )
        {
            EstimoteObject newObj = new EstimoteObject( sEstimoteValues[iObjIndex], "UNKNOWN_NAME", Constants.DEFAULT_PATTERN_INDEX, new ObjectConfig() );

            allObjects.add( newObj );
        }

        // Convert to Array and return
        return allObjects;
    }

}

