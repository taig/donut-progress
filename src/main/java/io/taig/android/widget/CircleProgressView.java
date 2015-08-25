package io.taig.android.widget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressView extends View {
    public interface OnProgressUpdateListener {
        String update( int current, int max );
    }

    private final CircleProgress view;
    
    private OnProgressUpdateListener listener;

    public CircleProgressView( Context context ) {
        super( context );
        view = new CircleProgress( context );
        init();
    }

    public CircleProgressView( Context context, AttributeSet attributes ) {
        super( context, attributes );
        view = new CircleProgress( context, attributes );
        init();
    }

    public CircleProgressView( Context context, AttributeSet attributes, int style ) {
        super( context, attributes, style );
        view = new CircleProgress( context, attributes, style );
        init();
    }
    
    private void init() {
        listener = view.progress().listener().getOrElse( null );
    }

    public void setCurrentProgress( int value ) { view.progress().current_$eq( value ); }
    
    public int getCurrentProgress() { return view.progress().current(); }
    
    public void setMaxProgress( int value ) { view.progress().max_$eq( value ); }
    
    public int getMaxProgress() { return view.progress().max(); }

    public void setProgressEndColor( @ColorRes int color ) { view.progress().color().end_$eq( color ); }

    public int getProgressEndColor() { return view.progress().color().end(); }
    
    public void setProgressStartColor( @ColorRes int color ) { view.progress().color().start_$eq( color ); }
    
    public int getProgressStartColor() { return view.progress().color().start(); }
    
    public void setOnProgressUpdateListener( @Nullable final OnProgressUpdateListener listener ) {
        if( listener == null ) {
            view.progress().listener_$eq( scala.Option.apply( ( CircleProgress.OnProgressChangedListener ) null ) );
            this.listener = null;
        }
        else {
            CircleProgress.OnProgressChangedListener wrapped = new CircleProgress.OnProgressChangedListener() {
                @Override
                public String update( int current, int max ) {
                    return listener.update( current, max );
                }
            };

            view.progress().listener_$eq( scala.Some.apply( wrapped ) );
            this.listener = listener;
        }
    }
    
    public OnProgressUpdateListener getOnProgressUpdateListener() { return listener; }

    public void setThickness( @Nullable Float value ) {
        if( value == null ) {
            view.thickness_$eq( scala.Option.apply( null ) );
        }
        else {
            view.thickness_$eq( scala.Some.apply( ( Object ) value ) );
        }
    }
    
    public Float getThickness() { return view.thickness().getOrElse( null ); }
    
    public void setLabelSize( @Nullable Float value ) {
        if( value == null ) {
            view.label().size_$eq( scala.Option.apply( null ) );
        }
        else {
            view.label().size_$eq( scala.Some.apply( ( Object ) value ) );
        }
    }
    
    public Float getLabelSize() { return view.label().size().getOrElse( null ); }
    
    public void setLabelDefaultColor( @ColorRes int color ) { view.label().color().default_$eq( color ); }
    
    public int getLabelDefaultColor() {
        // TODO keyword clash
        // view.label().color().default();
        return 0;
    }
    
    public void setLabelEmptyColor( @ColorRes int color ) { view.label().color().empty_$eq( color ); }

    public int getLabelEmptyColor() { return view.label().color().empty(); }
}