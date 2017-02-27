package com.example.fong.demo;

import android.os.Bundle;
import android.provider.ContactsContract.RawContacts.Data;

import java.util.Observable;

import static okhttp3.internal.Internal.instance;

/**
 * Created by administrator on 17/2/27.
 */

public class DataChange extends Observable {

    public static DataChange getInstance() {
        DataChange dataChange = new DataChange();
        return dataChange;
    }

    public void notifyDataChange(Data d ) {
        setChanged();
        notifyObservers(d);
    }
}
