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
package code.name.monkey.retromusic.fragments.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.mediarouter.app.MediaRouteButton
import code.name.monkey.appthemehelper.util.ColorUtil.isColorLight
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.MainActivity
import code.name.monkey.retromusic.fragments.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

abstract class AbsMainActivityFragment(@LayoutRes layout: Int) : AbsMusicServiceFragment(layout),
    MenuProvider {
    val libraryViewModel: LibraryViewModel by activityViewModel()

    val mainActivity: MainActivity
        get() = activity as MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

    @SuppressLint("PrivateResource")
    protected fun setCastColor(menu: Menu, toolbarColor: Int) {
        val castButton = menu.findItem(R.id.action_cast).actionView as MediaRouteButton
        val castContext = ContextThemeWrapper(context, androidx.mediarouter.R.style.Theme_MediaRouter)
        val attrs = castContext.obtainStyledAttributes(null, androidx.mediarouter.R.styleable.MediaRouteButton, androidx.mediarouter.R.attr.mediaRouteButtonStyle, 0)
        val drawable = attrs.getDrawable(androidx.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable)!!
        attrs.recycle()
        DrawableCompat.setTint(drawable, if(isColorLight(toolbarColor)) 0x8a000000.toInt() else 0x8affffff.toInt())
        drawable.state = castButton.drawableState
        castButton.setRemoteIndicatorDrawable(drawable)
    }
}
