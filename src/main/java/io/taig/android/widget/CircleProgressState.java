package io.taig.android.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.View.BaseSavedState;

public class CircleProgressState extends BaseSavedState {
    int current;

    int max;
    
    CircleProgressState( Parcelable superState ) {
        super( superState );
    }

    protected CircleProgressState( Parcel in ) {
        super( in );
        current = in.readInt();
        max = in.readInt();
    }

    @Override
    public void writeToParcel( Parcel out, int flags ) {
        super.writeToParcel( out, flags );
        out.writeInt( current );
        out.writeInt( max );
    }

    public static final Creator<CircleProgressState> CREATOR =
        new Creator<CircleProgressState>() {
            public CircleProgressState createFromParcel(Parcel in) {
                return new CircleProgressState( in );
            }
            public CircleProgressState[] newArray(int size) {
                return new CircleProgressState[size];
            }
        };
}