package com.example.myapplication

import android.content.Context
import android.os.Bundle
import barsoosayque.libgdxoboe.OboeAudio
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidAudio

// Presence of this kotlin file is embarrassing but I get odd build failures with scala
class MainActivity : AndroidApplication() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val config = AndroidApplicationConfiguration()
    config.useAccelerometer = true
    config.useCompass = true
    initialize(org.merlin.tertis.Tertis(), config)
  }

  // Android audio seemed to have perceptible latency so do this to maybe help.
  // https://libgdx.com/wiki/audio/audio#audio-on-android
  // https://github.com/barsoosayque/libgdx-oboe/blob/master/docs/Usage.md
  // this fork: https://github.com/pias-education/libgdx-oboe
  override fun createAudio(
    context: Context,
    config: AndroidApplicationConfiguration?
  ): AndroidAudio {
    return OboeAudio(context.assets)
  }
}
