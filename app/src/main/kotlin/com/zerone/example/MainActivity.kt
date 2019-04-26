package com.zerone.example

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : ListActivity() {
    private val tests = arrayOf("CannonTest",
                                 "CannonGravityTest",
                                 "CollisionTest",
                                 "Camera2DTest",
                                 "TextureAtlasTest",
                                 "SpriteBatcherTest",
                                 "AnimationTest",
                                 "SuperJumper",
                                 "Vertices3Test",
                                 "PerspectiveTest",
                                 "ZBufferTest",
                                 "ZBlendingTest",
                                 "CubeTest",
                                 "HierarchyTest",
                                 "LightTest",
                                 "EulerCameraTest",
                                 "ObjTest",
                                 "Invaders")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tests)
    }

    override fun onListItemClick(list: ListView, view: View, position: Int, id: Long) {
        super.onListItemClick(list, view, position, id)
        val testName = tests[position]
        try {
            val clazz = Class.forName("com.zerone.example.$testName")
            val intent = Intent(this, clazz)
            startActivity(intent)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
}
