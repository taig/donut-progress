package io.taig.android.dp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import io.taig.android.donout_progress.R;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * Circular progress widget
 */
public class DonutProgress extends View {
    interface OnProgressChangedListener {
        String update( int current, int max );
    }

    private Paint backgroundPaint;

    private Paint progressPaint;

    private int progressColorStart;

    private int progressColorEnd;

    private int progressCurrent;

    private int progressMax;

    private int progressStartAngle;

    private OnProgressChangedListener onProgressChangedListener = new OnProgressChangedListener() {
        @Override
        public String update( int current, int max ) {
            return Math.min( Math.round( (float) current / max * 100 ), 100 ) + "%";
        }
    };

    private int labelColorDefault;

    private int labelColorEmpty;

    private String labelText;

    private boolean labelSize = false;

    private float labelYOffset = 0;

    private Paint labelPaint;

    private boolean thickness = false;

    private RectF cacheRect = new RectF();

    public DonutProgress( Context context ) {
        super( context );
        init( context, null, 0, 0 );
    }

    public DonutProgress( Context context, AttributeSet attrs ) {
        super( context, attrs );
        init( context, attrs, 0, 0 );
    }

    public DonutProgress( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
        init( context, attrs, defStyleAttr, 0 );
    }

    @TargetApi( LOLLIPOP )
    public DonutProgress( Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes ) {
        super( context, attrs, defStyleAttr, defStyleRes );
        init( context, attrs, defStyleAttr, defStyleRes );
    }

    @SuppressWarnings( "deprecation" )
    private void init( Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes ) {
        TypedValue value = new TypedValue();
        Resources.Theme theme = context.getTheme();
        TypedArray array = theme.obtainStyledAttributes( attrs, R.styleable.DonutProgress, defStyleAttr, defStyleRes );

        try {
            int colorGrey;

            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                colorGrey = getContext().getColor( R.color.donut_progress_background );
            }
            else {
                colorGrey = getContext().getResources().getColor( R.color.donut_progress_background );
            }

            int colorText = new TextView( getContext() ).getTextColors().getDefaultColor();

            this.backgroundPaint = new Paint();
            this.backgroundPaint.setAntiAlias( true );
            this.backgroundPaint.setColor( colorGrey );
            this.backgroundPaint.setStyle( Paint.Style.STROKE );

            this.progressPaint = new Paint( this.backgroundPaint );

            this.progressColorStart = array.getColor( R.styleable.DonutProgress_donut_progress_colorStart, -1 );

            if( this.progressColorStart == -1 ) {
                theme.resolveAttribute( R.attr.colorPrimary, value, true );
                this.progressColorStart = value.data;
            }

            this.progressColorEnd = array.getColor( R.styleable.DonutProgress_donut_progress_colorEnd, -1 );

            if( this.progressColorEnd == -1 ) {
                theme.resolveAttribute( R.attr.colorPrimaryDark, value, true );
                this.progressColorEnd = value.data;
            }

            this.progressCurrent = array.getInt( R.styleable.DonutProgress_donut_progress_current, 0 );

            this.progressMax = array.getInt( R.styleable.DonutProgress_donut_progress_max, 100 );

            this.progressStartAngle = array.getInt( R.styleable.DonutProgress_donut_progress_startAngle, 0 );

            theme.resolveAttribute( android.R.attr.textColor, value, true );

            this.labelColorDefault = array.getColor(
                R.styleable.DonutProgress_donut_progress_labelColorDefault,
                -1
            );

            if( this.labelColorDefault == -1 ) {
                if( value.type != TypedValue.TYPE_NULL ) {
                    this.labelColorDefault = value.data;
                }
                else {
                    this.labelColorDefault = colorText;
                }
            }

            this.labelColorEmpty = array.getColor(
                R.styleable.DonutProgress_donut_progress_labelColorEmpty,
                colorGrey
            );

            this.labelText = render();

            this.labelPaint = new Paint();
            this.labelPaint.setAntiAlias( true );
            this.labelPaint.setTextAlign( Paint.Align.CENTER );

            float labelSize = array.getDimension( R.styleable.DonutProgress_donut_progress_labelSize, -1 );

            if( labelSize != -1 ) {
                setLabelSize( labelSize );
            }

            float thickness = array.getDimension( R.styleable.DonutProgress_donut_progress_thickness, -1 );

            if( thickness != -1 ) {
                setThickness( thickness );
            }
        }
        finally {
            array.recycle();
        }
    }

    /**
     * Use the {@link #onProgressChangedListener} to render the current progress to a String
     *
     * @return A String representation of the current progress, or <code>null</code> if {@link
     * #onProgressChangedListener} is null
     */
    private String render() {
        if( onProgressChangedListener != null ) {
            return onProgressChangedListener.update( progressCurrent, progressMax );
        }
        else {
            return null;
        }
    }

    /**
     * Get the progress ring start color
     * <p/>
     * The progress bar allows to render a circular gradient. This color is the gradient's start color.
     *
     * @return Progress ring start color value
     */
    public int getProgressStartColor() {
        return progressColorStart;
    }

    /**
     * Set the progress ring start color
     * <p/>
     * The progress bar allows to render a circular gradient. This color is the gradient's start color.
     * <p/>
     * Apply the same color value to {@link #setProgressStartColor(int)} and {@link #setProgressEndColor(int)} to
     * disable the gradient.
     *
     * @param color Color int value
     * @see #setProgressEndColor(int)
     */
    public void setProgressStartColor( @ColorInt int color ) {
        this.progressColorStart = color;
        invalidate();
    }

    /**
     * Get the progress ring end color
     * <p/>
     * The progress bar allows to render a circular gradient. This color is the gradient's end color.
     * <p/>
     * Apply the same color value to {@link #setProgressStartColor(int)} and {@link #setProgressEndColor(int)} to
     * disable the gradient.
     *
     * @return Progress ring end color value
     */
    public int getProgressEndColor() {
        return progressColorEnd;
    }

    /**
     * Set the progress ring end color
     * <p/>
     * The progress bar allows to render a circular gradient. This color is the gradient's end color.
     *
     * @param color Color int value
     * @see #setProgressStartColor(int)
     */
    public void setProgressEndColor( @ColorInt int color ) {
        this.progressColorEnd = color;
        invalidate();
    }

    /**
     * Get the current progress
     * <p/>
     * This is an absolute int value, not a percentage.
     *
     * @return Current progress value
     */
    public int getCurrentProgress() {
        return progressCurrent;
    }

    /**
     * Set the current progress
     *
     * @param value Current progress value
     * @throws IllegalArgumentException If value is < 0
     * @throws IllegalArgumentException If value is > max progress
     */
    public void setCurrentProgress( int value ) {
        if( value < 0 ) {
            throw new IllegalArgumentException( "Current progress must be >= 0" );
        }
        else if( value > progressMax ) {
            throw new IllegalArgumentException( "Current progress must be <= max progress" );
        }

        progressCurrent = value;
        labelText = render();
        invalidate();
    }

    /**
     * Get the max progress
     *
     * @return Max progress value
     */
    public int getMaxProgress() {
        return progressMax;
    }

    /**
     * Set the max progress
     *
     * @param value Max progress value
     * @throws IllegalArgumentException If value if < 0
     */
    public void setMaxProgress( int value ) {
        if( value < 0 ) {
            throw new IllegalArgumentException( "Max progress must be >= 0" );
        }

        if( progressCurrent > value ) {
            progressCurrent = value;
        }

        progressMax = value;
        labelText = render();
        invalidate();
    }

    /**
     * Get the progress ring start angle
     * <p/>
     * The angle defines where the progress ring starts to draw. By default it is set to 0, which is at the top.
     *
     * @return Progress ring start angle value
     */
    public int getProgressStartAngle() {
        return progressStartAngle;
    }

    /**
     * Set the progress ring start angle
     * <p/>
     * The angle defines where the progress ring starts to draw. By default it is set to 0, which is at the top.
     *
     * @param value Progress ring start angle value
     * @throws IllegalArgumentException If value < 0 || value > 360
     */
    public void setProgressStartAngle( int value ) {
        if( value < 0 || value > 360 ) {
            throw new IllegalArgumentException( "Value must be 0 <= x <= 360" );
        }

        progressStartAngle = value;
        invalidate();
    }

    /**
     * Get the {@link OnProgressChangedListener}
     *
     * @return {@link OnProgressChangedListener} or <code>null</code> if none applied
     */
    public OnProgressChangedListener getOnProgressChangedListener() {
        return onProgressChangedListener;
    }

    /**
     * Set the {@link OnProgressChangedListener}
     * <p/>
     * By default, a listener is set that renders percentage strings (such as "0%", "38%" or "100%").
     *
     * @param onProgressChangedListener Listener or <code>null</code> to disable label rendering
     */
    public void setOnProgressChangedListener( @Nullable OnProgressChangedListener onProgressChangedListener ) {
        this.onProgressChangedListener = onProgressChangedListener;
        labelText = render();
        invalidate();
    }

    /**
     * Get the default label color
     * <p/>
     * The default color is used when the current progress is > 0.
     *
     * @return Default label color value
     */
    public int getLabelDefaultColor() {
        return labelColorDefault;
    }

    /**
     * Set the default label color
     * <p/>
     * The default color is used when the current progress is > 0.
     *
     * @param color Default label color value
     */
    public void setLabelDefaultColor( @ColorInt int color ) {
        this.labelColorDefault = color;
        invalidate();
    }

    /**
     * Get the empty label color
     * <p/>
     * The empty color is used when the current progress is == 0.
     *
     * @return Empty label color value
     */
    public int getLabelEmptyColor() {
        return labelColorEmpty;
    }

    /**
     * Set the empty label color
     * <p/>
     * The empty color is used when the current progress is == 0.
     *
     * @param color Empty label color value
     */
    public void setLabelEmptyColor( @ColorInt int color ) {
        this.labelColorEmpty = color;
        invalidate();
    }

    /**
     * Get the currently displayed label text
     *
     * @return Currently displayed label text, or <code>null</code> if no text is visible
     */
    public String getLabelText() {
        return labelText;
    }

    /**
     * Get the label size
     *
     * @return Label size value, or <code>null</code> if no size is specified
     */
    public Float getLabelSize() {
        if( labelSize ) {
            return labelPaint.getTextSize();
        }
        else {
            return null;
        }
    }

    /**
     * Set the label size
     *
     * @param value Label size value, or <code>null</code> for automatic size (25% of the view's height)
     */
    public void setLabelSize( @Nullable Float value ) {
        if( value == null ) {
            labelSize = false;
        }
        else {
            labelSize = true;
            labelPaint.setTextSize( value );

            Rect rect = new Rect();
            labelPaint.getTextBounds( "100%", 0, 4, rect );
            labelYOffset = rect.height() / 2f;
        }

        invalidate();
    }

    /**
     * Get the ring thickness
     *
     * @return Thickness value, or <code>null</code> of no thickness is specified
     */
    public Float getThickness() {
        if( thickness ) {
            return backgroundPaint.getStrokeWidth();
        }
        else {
            return null;
        }
    }

    /**
     * Set the ring thickness
     *
     * @param value Thickness value, or <code>nukk</code> for automatic size (10% of the view's smallest dimension)
     */
    public void setThickness( @Nullable Float value ) {
        if( value == null ) {
            thickness = false;
        }
        else {
            backgroundPaint.setStrokeWidth( value );
            progressPaint.setStrokeWidth( value );
            thickness = true;
        }

        invalidate();
    }

    @Override
    protected void onSizeChanged( int width, int height, int oldWidth, int oldHeight ) {
        super.onSizeChanged( width, height, oldWidth, oldHeight );

        if( !labelSize ) {
            setLabelSize( height / 4f );
            labelSize = false;
        }

        if( !thickness ) {
            setThickness( Math.min( width, height ) / 10f );
            thickness = false;
        }
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        super.onDraw( canvas );

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        float margin = backgroundPaint.getStrokeWidth() / 2f;
        float progress = (float) progressCurrent / progressMax;

        cacheRect.left = margin;
        cacheRect.top = margin;
        cacheRect.right = width - margin;
        cacheRect.bottom = height - margin;

        progressPaint.setShader(
            new SweepGradient(
                width / 2f,
                height / 2f,
                new int[] { progressColorStart, progressColorEnd },
                new float[] { 0, progress }
            )
        );

        canvas.save();
        canvas.rotate( progressStartAngle - 90, width / 2f, height / 2f );
        canvas.drawArc( cacheRect, 0, 360, false, backgroundPaint );
        canvas.drawArc( cacheRect, 0, progress * 360, false, progressPaint );
        canvas.restore();

        if( onProgressChangedListener != null ) {
            if( progressCurrent == 0 ) {
                labelPaint.setColor( labelColorEmpty );
            }
            else {
                labelPaint.setColor( labelColorDefault );
            }

            canvas.drawText(
                labelText,
                width / 2f,
                height / 2f + labelYOffset,
                labelPaint
            );
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        State state = new State( super.onSaveInstanceState() );

        state.current = progressCurrent;
        state.max = progressMax;

        return state;
    }

    @Override
    protected void onRestoreInstanceState( Parcelable parcelable ) {
        if( parcelable instanceof State ) {
            State state = ( (State) parcelable );

            progressCurrent = state.current;
            progressMax = state.max;

            super.onRestoreInstanceState( state.getSuperState() );
        }
        else {
            super.onRestoreInstanceState( parcelable );
        }
    }

    public static class State extends BaseSavedState {
        int current;

        int max;

        State( Parcelable superState ) {
            super( superState );
        }

        protected State( Parcel in ) {
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

        public static final Creator<State> CREATOR =
            new Creator<State>() {
                public State createFromParcel( Parcel in ) {
                    return new State( in );
                }

                public State[] newArray( int size ) {
                    return new State[size];
                }
            };
    }
}