package com.villapp.jesussai.app

import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.ar.sceneform.HitTestResult
import com.villapp.jesussai.BuildConfig
import com.villapp.jesussai.R
import com.villapp.jesussai.ar.AugmentedImageNode

class MainActivity : ArBaseActivity() {
    private lateinit var arFragment: CustomArFragment
    private var selectNode: AugmentedImageNode? = null
    override val viewId: Int = R.layout.activity_main

    override fun startAr() {
        val selection = intent.getStringExtra("selector")
        arFragment = CustomArFragment(selection)

        supportFragmentManager.beginTransaction().replace(R.id.ar_fragment, arFragment).commit()

        if (BuildConfig.DEBUG) {
//            arFragment.setOnStarted = {
//                arFragment.arSceneView.scene.setOnTouchListener(::handleTouch)
//
//                debugInit()
//            }
        }
    }

    private fun handleTouch(hitTestResult: HitTestResult, event: MotionEvent): Boolean {
        if (selectNode != hitTestResult.node && hitTestResult.node is AugmentedImageNode) {
            selectNode = hitTestResult.node as AugmentedImageNode
            Toast.makeText(this, "${selectNode?.name} selected", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    private fun debugInit() {
        findViewById<View>(R.id.debug_panel).visibility = View.VISIBLE
        val offset = 0.001f
        findViewById<View>(R.id.add_x).setOnClickListener {
            selectNode?.modifyLayout {
                offsetX += offset
            }
        }
        findViewById<View>(R.id.add_y).setOnClickListener {
            selectNode?.modifyLayout {
                offsetY += offset
            }
        }
        findViewById<View>(R.id.add_z).setOnClickListener {
            selectNode?.modifyLayout {
                offsetZ += offset
            }
        }
        findViewById<View>(R.id.minus_x).setOnClickListener {
            selectNode?.modifyLayout {
                offsetX -= offset
            }
        }
        findViewById<View>(R.id.minus_y).setOnClickListener {
            selectNode?.modifyLayout {
                offsetY -= offset
            }
        }
        findViewById<View>(R.id.minus_z).setOnClickListener {
            selectNode?.modifyLayout {
                offsetZ -= offset
            }
        }

        findViewById<View>(R.id.scale_up).setOnClickListener {
            selectNode?.modifyLayout {
                scaledWidth += offset
                scaledHeight += offset
                scaledDeep += offset
            }
        }

        findViewById<View>(R.id.scale_down).setOnClickListener {
            selectNode?.modifyLayout {
                scaledWidth -= offset
                scaledHeight -= offset
                scaledDeep -= offset
            }
        }

    }
}

