package com.villapp.jesussai.app

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.google.ar.core.*
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ExternalTexture
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.villapp.jesussai.R
import com.villapp.jesussai.ar.AugmentedImageAnchorNode
import com.villapp.jesussai.ar.ArResources
import com.villapp.jesussai.ar.CustomAnchorNode
import com.villapp.jesussai.utils.Logger

@SuppressLint("ValidFragment")
class CustomArFragment(private val selection:String) : ArFragment() {
    private val trackableMap = mutableMapOf<String, AugmentedImageAnchorNode>()

    var setOnStarted: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view!!.visibility = View.GONE

        // Turn off the plane discovery since we're only looking for ArImages
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false
        arSceneView.scene.setOnTouchListener { _, motionEvent ->
            swipeAnGestureDetector.onTouchEvent(motionEvent)
        }

        arSceneView.scene.addOnUpdateListener(::onUpdateFrame)

        ArResources.init(this.context!!, this.selection).handle { _, _ ->
            setOnStarted?.invoke()

            view.visibility = View.VISIBLE
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        trackableMap.forEach {
            arSceneView.scene.removeChild(it.value)
        }

        trackableMap.clear()
    }

    override fun getSessionConfiguration(session: Session): Config {
        val config = super.getSessionConfiguration(session)
        config.focusMode = Config.FocusMode.AUTO

        config.augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, context!!.resources.assets.open("ar.imgdb"))

        return config
    }

    private fun createArNode(image: AugmentedImage) {
        Logger.d("create : ${image.name}(${image.index}), pose: ${image.centerPose}, ex: ${image.extentX}, ez: ${image.extentZ}")

        when (image.name) {
            this.selection -> {
                when (image.name) {
                    "pez.jpeg" -> {
                        val node = CustomAnchorNode(1.3F, 2F).init(image)
                        trackableMap[image.name] = node
                        arSceneView.scene.addChild(node)
                    }
                    "paloma.jpg" -> {
                        val node = CustomAnchorNode(1.3F, 1.1F).init(image)
                        trackableMap[image.name] = node
                        arSceneView.scene.addChild(node)
                    }
                    "catrinas.jpg" -> {
                        val node = CustomAnchorNode(1.3F, 1.5F).init(image)
                        trackableMap[image.name] = node
                        arSceneView.scene.addChild(node)
                    }
                }

            }
        }
        Toast.makeText(context, "${image.name} added", Toast.LENGTH_LONG).show()
    }

    private fun onUpdateFrame(@Suppress("UNUSED_PARAMETER") frameTime: FrameTime?) {
        val frame = arSceneView.arFrame

        // If there is no frame or ARCore is not tracking yet, just return.
        if (frame == null || frame.camera.trackingState != TrackingState.TRACKING) {
            return
        }

        frame.getUpdatedTrackables(AugmentedImage::class.java).forEach { image ->
            when (image.trackingState) {
                TrackingState.TRACKING -> if (trackableMap.contains(image.name)) {
                    if (trackableMap[image.name]?.update(image) == true) {
                        Logger.d("update node: ${image.name}(${image.index}), pose: ${image.centerPose}, ex: ${image.extentX}, ez: ${image.extentZ}")
                    }
                } else {
                    createArNode(image)
                }
                TrackingState.STOPPED -> {
                    Logger.d("remove node: ${image.name}(${image.index})")
                    Toast.makeText(context, "${image.name} removed", Toast.LENGTH_LONG).show()

                        trackableMap.remove(image.name).let {
                        arSceneView.scene.removeChild(it)
                    }
                }
                else -> {
                }
            }
        }
    }

    private val swipeAnGestureDetector = GestureDetector(null, object : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_DISTANCE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val swarmAN = trackableMap["swarm"] as? CustomAnchorNode

            if (swarmAN != null && swarmAN.isActive) {
                val distanceX = e2.x - e1.x
                val distanceY = e2.y - e1.y
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0) {
                        swarmAN.forwardScene()
                    } else {
                        swarmAN.backwardScene()
                    }

                    return true
                }
            }

            return false
        }
    })
}