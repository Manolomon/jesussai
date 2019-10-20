package com.villapp.jesussai.ar

import com.villapp.jesussai.utils.Logger

class CustomAnchorNode(override val imageWidth: Float, override val imageHeight: Float) : AugmentedImageAnchorNode() {

    private val sceneList = mutableListOf<AugmentedImageNodeGroup>()
    private var currentSceneIndex = 0

    override fun onInit() {
        //sceneList.add(SwarmScene1().init(this))
        sceneList.add(SwarmScene2().init(this))
        //sceneList.add(SwarmScene3().init(this))
    }

    override fun onActivate() {
        super.onActivate()

        changeScene(0)
    }

    fun forwardScene() {
        changeScene((currentSceneIndex + 1) % sceneList.size)
    }

    fun backwardScene() {
        changeScene((currentSceneIndex - 1 + sceneList.size) % sceneList.size)
    }

    private fun changeScene(index: Int) {
        currentSceneIndex = index

        Logger.d("changeScene($currentSceneIndex)")

        sceneList.forEachIndexed { i, scene ->
            scene.isEnabled = i == currentSceneIndex
        }
    }
}