/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */
package com.orane.icliniq.chime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.services.chime.sdk.meetings.audiovideo.AudioVideoFacade
import com.orane.icliniq.R
import com.orane.icliniq.chime.data.VideoCollectionTile
import kotlinx.android.synthetic.main.video_collection_item.view.*

class VideoCollectionTileAdapter(
        private val videoCollectionTiles: MutableCollection<VideoCollectionTile>,
        private val audioVideoFacade: AudioVideoFacade,
        private val context: Context?
) :
    RecyclerView.Adapter<ViewHolder>() {
    private val VIDEO_ASPECT_RATIO_16_9 = 1.5625
    private val PADDING_RATIO = 0.9

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.video_collection_item, parent, false)
        return ViewHolder(inflatedView, audioVideoFacade)
    }

    override fun getItemCount(): Int {
        return videoCollectionTiles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoCollectionTile = videoCollectionTiles.elementAt(position)
        holder.bindVideoTile(videoCollectionTile)
        context?.let {
            val displayMetrics = context.resources.displayMetrics
            val width = displayMetrics.widthPixels * PADDING_RATIO
            val height = (width * VIDEO_ASPECT_RATIO_16_9).toInt()
            holder.tileContainer.layoutParams.height = height
            holder.tileContainer.layoutParams.width = width.toInt()
        }
    }
}

class ViewHolder(inflatedView: View, audioVideoFacade: AudioVideoFacade) :
    RecyclerView.ViewHolder(inflatedView) {

    private var view: View = inflatedView
    private var audioVideo = audioVideoFacade
    val tileContainer: FrameLayout = view.findViewById(R.id.tile_container)

    init {
        setIsRecyclable(false)
    }

    fun bindVideoTile(videoCollectionTile: VideoCollectionTile) {
        audioVideo.bindVideoView(view.video_surface, videoCollectionTile.videoTileState.tileId)
        view.video_surface.contentDescription = "${videoCollectionTile.attendeeName} VideoTile"
        if (videoCollectionTile.videoTileState.isLocalTile) {
            view.switch_camera.visibility = View.VISIBLE
            view.switch_camera.setOnClickListener { audioVideo.switchCamera() }
        } else {
            view.attendee_name.text = videoCollectionTile.attendeeName
            view.attendee_name.visibility = View.VISIBLE
        }
    }
}
