package io.taig.android.widget.test

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.widget.DonutProgress
import org.robolectric.{ Robolectric, RuntimeEnvironment }
import org.robolectric.annotation.Config
import org.scalatest.{ Matchers, FlatSpec, RobolectricSuite }

@Config( sdk = Array( LOLLIPOP ) )
class Test
        extends FlatSpec
        with Matchers
        with RobolectricSuite {
    implicit val context = RuntimeEnvironment.application

    "CircleProgress" should "have its current progress set to zero initially" in {
        new DonutProgress( context ).getCurrentProgress shouldBe 0
    }

    it should "have its max progress set to 100 initially" in {
        new DonutProgress( context ).getMaxProgress shouldBe 100
    }
    
    it should "have its progress angle set to 0 initially" in {
        new DonutProgress( context ).getProgressStartAngle shouldBe 0
    }

    it should "restore the current and max progress after recreation" in {
        val activity = Robolectric.buildActivity( classOf[Activity] ).create( null ).get

        val widget1 = activity.findViewById( 1 ).asInstanceOf[DonutProgress]

        widget1.setCurrentProgress( 25 )
        widget1.setMaxProgress( 500 )

        activity.recreate()

        val widget2 = activity.findViewById( 1 ).asInstanceOf[DonutProgress]

        widget1 should not be theSameInstanceAs( widget2 )
        widget1.getCurrentProgress shouldBe widget2.getCurrentProgress
        widget1.getMaxProgress shouldBe widget2.getMaxProgress
    }
}