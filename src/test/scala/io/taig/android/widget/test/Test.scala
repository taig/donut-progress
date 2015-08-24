package io.taig.android.widget.test

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.widget.CircleProgress
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
        CircleProgress().progress.current shouldBe 0
    }

    it should "have its max progress set to 100 initially" in {
        CircleProgress().progress.max shouldBe 100
    }

    it should "restore the current and max progress after recreation" in {
        val activity = Robolectric.buildActivity( classOf[Activity] ).create( null ).get

        val widget1 = activity.findViewById( 1 ).asInstanceOf[CircleProgress]

        widget1.progress.current = 25
        widget1.progress.max = 500

        activity.recreate()

        val widget2 = activity.findViewById( 1 ).asInstanceOf[CircleProgress]

        widget1 should not be theSameInstanceAs( widget2 )
        widget1.progress.current shouldBe widget2.progress.current
        widget1.progress.max shouldBe widget2.progress.max
    }
}