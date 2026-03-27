/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.fragments.player.blur

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.FragmentBlurPlayerProgressControlsBinding
import code.name.monkey.retromusic.extensions.applyColor
import code.name.monkey.retromusic.extensions.getSongInfo
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.fragments.base.AbsPlayerProgressFragment
import code.name.monkey.retromusic.fragments.base.goToAlbum
import code.name.monkey.retromusic.fragments.base.goToArtist
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import com.google.android.material.slider.Slider

class BlurPlaybackProgressFragment :
    AbsPlayerProgressFragment(R.layout.fragment_blur_player_progress_controls) {
    private var _binding: FragmentBlurPlayerProgressControlsBinding? = null
    private val binding get() = _binding!!

    override val progressSlider: Slider
        get() = binding.progressSlider

    override val songTotalTime: TextView
        get() = binding.songTotalTime

    override val songCurrentProgress: TextView
        get() = binding.songCurrentProgress

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBlurPlayerProgressControlsBinding.bind(view)
        binding.title.isSelected = true
        binding.text.isSelected = true
        binding.title.setOnClickListener {
            goToAlbum(requireActivity())
        }
        binding.text.setOnClickListener {
            goToArtist(requireActivity())
        }
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        binding.title.text = song.title
        binding.text.text = String.format("%s • %s", song.artistName, song.albumName)

        if (PreferenceUtil.isSongInfo) {
            binding.songInfo.show()
            binding.songInfo.text = getSongInfo(song)
        } else {
            binding.songInfo.hide()
        }
    }

    override fun onServiceConnected() {
        updateSong()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
    }

    override fun setColor(color: MediaNotificationProcessor) {
        lastPlaybackControlsColor = Color.WHITE
        lastDisabledPlaybackControlsColor =
            ContextCompat.getColor(requireContext(), code.name.monkey.appthemehelper.R.color.md_grey_500)

        binding.title.setTextColor(lastPlaybackControlsColor)

        binding.songCurrentProgress.setTextColor(lastPlaybackControlsColor)
        binding.songTotalTime.setTextColor(lastPlaybackControlsColor)

        binding.text.setTextColor(lastPlaybackControlsColor)
        binding.songInfo.setTextColor(lastDisabledPlaybackControlsColor)

        binding.progressSlider.applyColor(lastPlaybackControlsColor)
        volumeFragment?.setTintableColor(lastPlaybackControlsColor)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
