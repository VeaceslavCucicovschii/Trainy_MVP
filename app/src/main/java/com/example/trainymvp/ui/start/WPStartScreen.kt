package com.example.trainymvp.ui.start

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.trainymvp.R
import com.example.trainymvp.navigation.NavigationDestination
import com.example.trainymvp.ui.AppViewModelProvider

object WPStartDestination : NavigationDestination {
    override val route = "wp_start"
    override val titleRes = R.string.wp_start_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun WPStartScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: WPStartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    val images = viewModel.imagesUiState.images
    val imageIndex = viewModel.timerUiState.imageIndex

    if (images.isNotEmpty() && imageIndex in images.indices) {
        Image(
            painter = rememberAsyncImagePainter(images[imageIndex]),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        // Optional: show a loading placeholder or error image
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_extra_large))
    ) {
        InfoCard("70%") // TODO
        Spacer(Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
        InfoCard("45s") // TODO
    }

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_extra_large))
    ) {
        InfoCard(
            text = if(viewModel.timerUiState.isPaused) "Rest" else "Exercise",
            variant = "small"
        )
    }
}

@Composable
fun InfoCard(
    text: String,
    variant: String = "default",
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(32),
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            style = if(variant == "small") MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}