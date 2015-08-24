package io.taig.android.widget.test

import android.os.Bundle
import android.widget.FrameLayout
import io.taig.android.widget.CircleProgress

class Activity extends android.app.Activity {
    override def onCreate( savedInstanceState: Bundle ) = {
        super.onCreate( savedInstanceState )

        setContentView {
            new FrameLayout( this ) {
                addView {
                    new CircleProgress( Activity.this ) {
                        setId( 1 )
                    }
                }
            }
        }
    }
}