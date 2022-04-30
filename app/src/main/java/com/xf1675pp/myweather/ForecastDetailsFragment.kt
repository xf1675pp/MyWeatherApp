package com.xf1675pp.myweather

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.xf1675pp.myweather.data.DayForecast
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher


class ForecastDetailsFragment : Fragment() {

    lateinit var forecast: DayForecast
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        forecast = requireArguments().get("forecast_details") as DayForecast
        val iconUrl = "https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png"
        val image = Glide.with(requireContext()).asDrawable().load(iconUrl).submit(300, 300)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    val image = DownloadImage(iconUrl).value
                    image?.let { img ->
                        AddImage(bitmap = image.asImageBitmap())
                    }
                    AddText(text = "Weather Condition : ${forecast.weather[0].main}")
                    AddText(text = "Day Temperature : ${forecast.temp.day}°")
                    AddText(text = "Low Temperature : ${forecast.temp.min}°")
                    AddText(text = "High Temperature : ${forecast.temp.max}°")
                    AddText(text = "Humidity : ${forecast.humidity}°")
                    AddText(text = "Pressure : ${forecast.pressure} hPa")
                    AddText(text = "Wind Speed : ${forecast.speed} m/s")
                }
            }
        }
    }

    @Composable
    fun AddImage(bitmap: ImageBitmap) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = Modifier
                .height(160.dp)
                .width(160.dp)
                .padding(0.dp),
            contentScale = ContentScale.Crop
        )
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun DownloadImage(image: String): MutableState<Bitmap?> {

        val bitmap: MutableState<Bitmap?> = mutableStateOf(null)

        Glide.with(requireContext())
            .asBitmap()
            .load(R.drawable.sun_icon)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap.value = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

            })

        Glide.with(requireContext())
            .asBitmap()
            .load(image)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap.value = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

            })
//            Image(
//                painter = rememberDrawablePainter(drawable = image.get()),
//                contentDescription = null,
//                modifier = Modifier
//                    .height(160.dp)
//                    .width(160.dp)
//                    .padding(0.dp),
//                contentScale = ContentScale.Crop
//            )


        return bitmap
    }

    @Composable
    fun AddText(text: String) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(
                    top = 4.dp
                )
                .fillMaxWidth()

        )
    }


}