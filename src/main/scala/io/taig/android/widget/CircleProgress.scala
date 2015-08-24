package io.taig.android.widget

import android.content.Context
import android.graphics._
import android.os.{ Build, Parcelable }
import android.util.{ AttributeSet, TypedValue }
import android.view.View
import android.widget.TextView
import io.taig.android.donout_progress.R
import io.taig.android.widget.CircleProgress.OnProgressChangedListener

import scala.language.reflectiveCalls
import scala.math._

case class CircleProgress( attrs: AttributeSet = null, style: Int = 0 )( implicit context: Context )
        extends View( context, attrs, style ) {
    private object attributes {
        object color {
            val grey = {
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                    context.getColor( R.color.grey )
                }
                else {
                    context.getResources.getColor( R.color.grey )
                }
            }

            val text = new TextView( context ).getTextColors.getDefaultColor
        }

        /**
         * Background ring attributes
         */
        object background {
            val paint = new Paint() {
                setAntiAlias( true )
                setColor( color.grey )
                setStyle( Paint.Style.STROKE )
            }
        }

        /**
         * Foreground progress donut attributes
         */
        object progress {
            object color {
                var start = attributes.color.text

                var end = attributes.color.text
            }

            var current = 0

            var max = 100

            val paint = new Paint( background.paint )

            var listener: Option[OnProgressChangedListener] = Some(
                new OnProgressChangedListener {
                    override def update( current: Int, max: Int ) = {
                        min( round( current.toFloat / max * 100 ), 100 ) + "%"
                    }
                }
            )
        }

        /**
         * Label text attributes
         */
        object label {
            object color {
                var default = attributes.color.text

                var empty = attributes.color.grey
            }

            var text = render()

            /**
             * Whether or not the user defined a fixed text size
             */
            var size = false

            /**
             * Y offset for vertical alignment
             */
            var offset = 0f

            val paint: Paint = new Paint() {
                setAntiAlias( true )
                setTextAlign( Paint.Align.CENTER )
            }

            def render() = progress.listener.map( _.update( progress.current, progress.max ) )
        }

        val rect = new RectF()

        /**
         * Whether or not the user defined a custom thikness value
         */
        var thickness = false

        val rings = Seq[{ def paint: Paint }]( background, progress ).map( _.paint )
    }

    def this( context: Context, attributes: AttributeSet, style: Int ) = this( attributes, style )( context )

    def this( context: Context, attributes: AttributeSet ) = this( attributes )( context )

    def this( context: Context ) = this()( context )

    {
        // Initialize label text color
        val value = new TypedValue()
        context.getTheme.resolveAttribute( android.R.attr.textColor, value, true )

        if ( value.`type` != TypedValue.TYPE_NULL ) {
            attributes.label.color.default = value.data
        }

    }

    {
        // Initialize progress gradient colors
        val value = new TypedValue()

        context.getTheme.resolveAttribute( R.attr.colorPrimary, value, true )
        attributes.progress.color.start = value.data

        context.getTheme.resolveAttribute( R.attr.colorPrimaryDark, value, true )
        attributes.progress.color.end = value.data
    }

    object progress {
        object color {
            def end_=( color: Int ) = {
                attributes.progress.color.end = color
            }

            def end = attributes.progress.color.end

            def start_=( color: Int ) = {
                attributes.progress.color.start = color
            }

            def start = attributes.progress.color.start
        }

        def listener_=( listener: Option[OnProgressChangedListener] ) = {
            attributes.progress.listener = listener
        }

        def listener = attributes.progress.listener

        def max_=( value: Int ) = {
            attributes.progress.max = max
            attributes.label.text = attributes.label.render()
        }

        def max = attributes.progress.max

        def current_=( value: Int ) = {
            attributes.progress.current = value
            attributes.label.text = attributes.label.render()
        }

        def current = attributes.progress.current
    }

    object label {
        object color {
            def default_=( color: Int ) = {
                attributes.label.color.default = color
            }

            def default = attributes.label.color.default

            def empty_=( color: Int ) = {
                attributes.label.color.empty = color
            }

            def empty = attributes.label.color.empty
        }

        def size_=( size: Option[Float] ) = size match {
            case Some( size ) ⇒
                attributes.label.size = true
                attributes.label.paint.setTextSize( size )
                attributes.label.offset = {
                    val rect = new Rect()
                    attributes.label.paint.getTextBounds( "100%", 0, 4, rect )
                    rect.height() / 2f
                }
            case None ⇒ attributes.label.size = false
        }

        def size = if ( attributes.label.size ) Some( attributes.label.paint.getTextSize ) else None
    }

    def thickness_=( value: Option[Float] ) = value match {
        case Some( thickness ) ⇒
            attributes.rings.foreach( _.setStrokeWidth( thickness ) )
            attributes.thickness = true
        case None ⇒ attributes.thickness = false
    }

    def thickness = if ( attributes.thickness ) Some( attributes.background.paint.getStrokeWidth ) else None

    override def onSizeChanged( width: Int, height: Int, oldWidth: Int, oldHeight: Int ) = {
        super.onSizeChanged( width, height, oldWidth, oldHeight )

        if ( !attributes.label.size ) {
            label.size = Some( height / 4f )
            attributes.label.size = false
        }

        if ( !attributes.thickness ) {
            thickness = Some( min( width, height ) / 10f )
            attributes.thickness = false
        }
    }

    override def onDraw( canvas: Canvas ) = {
        super.onDraw( canvas )

        val width = canvas.getWidth
        val height = canvas.getHeight
        val margin = attributes.background.paint.getStrokeWidth / 2f
        val percentage = attributes.progress.current.toFloat / attributes.progress.max

        attributes.rect.left = margin
        attributes.rect.top = margin
        attributes.rect.right = width - margin
        attributes.rect.bottom = height - margin

        attributes.progress.paint.setShader(
            new SweepGradient(
                width / 2f,
                height / 2f,
                Array( progress.color.start, progress.color.end ),
                Array( 0, percentage )
            )
        )

        canvas.save()
        canvas.rotate( -90f, width / 2f, height / 2f )
        canvas.drawArc( attributes.rect, 0, 360, false, attributes.background.paint )
        canvas.drawArc( attributes.rect, 0, percentage * 360, false, attributes.progress.paint )
        canvas.restore()

        progress.listener.foreach( listener ⇒ {
            attributes.label.paint.setColor( if ( attributes.progress.current == 0 ) label.color.empty else label.color.default )

            canvas.drawText(
                listener.update( progress.current, progress.max ),
                width / 2f,
                height / 2f + attributes.label.offset,
                attributes.label.paint
            )
        } )
    }

    override def onSaveInstanceState() = {
        val state = new CircleProgressState( super.onSaveInstanceState() )
        state.current = progress.current
        state.max = progress.max
        state
    }

    override def onRestoreInstanceState( state: Parcelable ) = state match {
        case state: CircleProgressState ⇒
            super.onRestoreInstanceState( state.getSuperState )
            progress.current = state.current
            progress.max = state.max
        case state ⇒ super.onRestoreInstanceState( state )
    }
}

object CircleProgress {
    trait OnProgressChangedListener {
        def update( current: Int, max: Int ): String
    }
}