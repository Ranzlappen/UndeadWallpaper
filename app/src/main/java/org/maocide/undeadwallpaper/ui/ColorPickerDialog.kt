package org.maocide.undeadwallpaper.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.maocide.undeadwallpaper.R
import org.maocide.undeadwallpaper.databinding.DialogColorPickerBinding
import java.util.Locale

/**
 * A small, dependency-free color picker built on [MaterialAlertDialogBuilder] (the codebase's
 * existing dialog pattern). Offers a row of preset swatches plus RGB sliders with a live preview.
 */
object ColorPickerDialog {

    // A compact palette of common accent colors (opaque ARGB).
    private val PRESETS = intArrayOf(
        0xFF3CDC84.toInt(), // zombie green (app default)
        0xFFF44336.toInt(), // red
        0xFFE91E63.toInt(), // pink
        0xFF9C27B0.toInt(), // purple
        0xFF3F51B5.toInt(), // indigo
        0xFF2196F3.toInt(), // blue
        0xFF009688.toInt(), // teal
        0xFFFF9800.toInt(), // orange
        0xFFFFEB3B.toInt(), // yellow
        0xFF9E9E9E.toInt()  // grey
    )

    fun show(context: Context, initialColor: Int, onColorSelected: (Int) -> Unit) {
        val binding = DialogColorPickerBinding.inflate(LayoutInflater.from(context))

        // current[0] holds the live-selected color, updated by sliders and presets.
        val current = intArrayOf(initialColor or 0xFF000000.toInt())

        fun refreshPreview() {
            val c = current[0]
            binding.colorPreview.setBackgroundColor(c)
            binding.hexValue.text = String.format(
                Locale.US, "#%06X", c and 0x00FFFFFF
            )
        }

        // Initialize sliders from the starting color.
        binding.seekRed.progress = Color.red(current[0])
        binding.seekGreen.progress = Color.green(current[0])
        binding.seekBlue.progress = Color.blue(current[0])
        refreshPreview()

        val sliderListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                current[0] = Color.rgb(
                    binding.seekRed.progress,
                    binding.seekGreen.progress,
                    binding.seekBlue.progress
                )
                refreshPreview()
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        }
        binding.seekRed.setOnSeekBarChangeListener(sliderListener)
        binding.seekGreen.setOnSeekBarChangeListener(sliderListener)
        binding.seekBlue.setOnSeekBarChangeListener(sliderListener)

        // Build preset swatches programmatically.
        val sizePx = (40 * context.resources.displayMetrics.density).toInt()
        val marginPx = (4 * context.resources.displayMetrics.density).toInt()
        for (preset in PRESETS) {
            val swatch = View(context)
            val lp = LinearLayout.LayoutParams(sizePx, sizePx).apply {
                marginEnd = marginPx
            }
            swatch.layoutParams = lp
            swatch.background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(preset)
                setStroke(2, Color.DKGRAY)
            }
            swatch.setOnClickListener {
                current[0] = preset or 0xFF000000.toInt()
                binding.seekRed.progress = Color.red(current[0])
                binding.seekGreen.progress = Color.green(current[0])
                binding.seekBlue.progress = Color.blue(current[0])
                refreshPreview()
            }
            binding.presetContainer.addView(swatch)
        }

        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.color_picker_title)
            .setView(binding.root)
            .setPositiveButton(R.string.color_picker_ok) { _, _ ->
                onColorSelected(current[0])
            }
            .setNegativeButton(R.string.color_picker_cancel, null)
            .show()
    }
}
