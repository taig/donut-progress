package io.taig.android.widget.test

import android.os.Bundle
import android.widget.FrameLayout
import io.taig.android.widget.DonutProgress

class Activity extends android.app.Activity {
    override def onCreate( savedInstanceState: Bundle ) = {
        super.onCreate( savedInstanceState )

        setContentView {
            new FrameLayout( this ) {
                addView {
                    new DonutProgress( Activity.this ) {
                        setId( 1 )
                    }
                }
            }
        }
    }
}