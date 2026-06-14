package org.maocide.undeadwallpaper.ui

import org.maocide.undeadwallpaper.R
import org.maocide.undeadwallpaper.data.ImageFileManager
import org.maocide.undeadwallpaper.model.BridgeMode
import org.maocide.undeadwallpaper.model.ScreenSlot

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

/**
 * Lists the per-screen "slots". Each row shows the chosen video (name + thumbnail),
 * supports drag-to-reorder via the handle, and—in [BridgeMode.PER_PAGE_IMAGE]—a
 * per-page bridge image picker.
 *
 * @param videoThumbnailProvider returns a cached thumbnail for a video file name
 *        (backed by the playlist's already-generated thumbnails), or null.
 * @param onStartDrag invoked when the drag handle is touched, so the host can ask
 *        the ItemTouchHelper to begin the drag.
 */
class ScreenSlotAdapter(
    private val slots: MutableList<ScreenSlot>,
    private val imageFileManager: ImageFileManager,
    private var bridgeMode: BridgeMode,
    private val videoThumbnailProvider: (String) -> Bitmap?,
    private val onChooseVideo: (Int) -> Unit,
    private val onChooseImage: (Int) -> Unit,
    private val onRemove: (Int) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<ScreenSlotAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.screen_slot_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(slots[position], position)
    }

    override fun getItemCount(): Int = slots.size

    fun setBridgeMode(mode: BridgeMode) {
        bridgeMode = mode
        notifyDataSetChanged()
    }

    fun getSlots(): List<ScreenSlot> = slots

    fun updateSlots(newSlots: List<ScreenSlot>) {
        slots.clear()
        slots.addAll(newSlots)
        notifyDataSetChanged()
    }

    /** Moves a slot during a drag; the host persists the new order on drop. */
    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) Collections.swap(slots, i, i + 1)
        } else {
            for (i in fromPosition downTo toPosition + 1) Collections.swap(slots, i, i - 1)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dragHandle: ImageView = itemView.findViewById(R.id.drag_handle)
        private val screenNumber: TextView = itemView.findViewById(R.id.text_screen_number)
        private val videoThumbnail: ImageView = itemView.findViewById(R.id.image_video_thumbnail)
        private val chooseVideo: MaterialButton = itemView.findViewById(R.id.button_choose_video)
        private val selectedVideo: TextView = itemView.findViewById(R.id.text_selected_video)
        private val perPageImageLayout: View = itemView.findViewById(R.id.layout_per_page_image)
        private val chooseImage: MaterialButton = itemView.findViewById(R.id.button_choose_image)
        private val imageThumbnail: ImageView = itemView.findViewById(R.id.image_slot_thumbnail)
        private val removeButton: MaterialButton = itemView.findViewById(R.id.button_remove_screen)

        @SuppressLint("ClickableViewAccessibility")
        fun bind(slot: ScreenSlot, position: Int) {
            val context = itemView.context
            screenNumber.text = context.getString(R.string.per_screen_screen_number, position + 1)

            selectedVideo.text = slot.videoFileName
                ?: context.getString(R.string.per_screen_no_video)

            // Video thumbnail (reuses the playlist's already-generated thumbnails).
            val videoBmp = slot.videoFileName?.let { videoThumbnailProvider(it) }
            if (videoBmp != null) {
                videoThumbnail.setImageBitmap(videoBmp)
                videoThumbnail.visibility = View.VISIBLE
            } else {
                videoThumbnail.setImageDrawable(null)
                videoThumbnail.visibility = View.GONE
            }

            chooseVideo.setOnClickListener { onChooseVideo(bindingAdapterPosition) }
            removeButton.setOnClickListener { onRemove(bindingAdapterPosition) }

            dragHandle.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    onStartDrag(this)
                }
                false
            }

            if (bridgeMode == BridgeMode.PER_PAGE_IMAGE) {
                perPageImageLayout.visibility = View.VISIBLE
                chooseImage.setOnClickListener { onChooseImage(bindingAdapterPosition) }

                val bmp = imageFileManager.loadBitmap(slot.bridgeImageFileName, 240, 240)
                if (bmp != null) {
                    imageThumbnail.setImageBitmap(bmp)
                    imageThumbnail.visibility = View.VISIBLE
                } else {
                    imageThumbnail.setImageDrawable(null)
                    imageThumbnail.visibility = View.GONE
                }
            } else {
                perPageImageLayout.visibility = View.GONE
            }
        }
    }
}
