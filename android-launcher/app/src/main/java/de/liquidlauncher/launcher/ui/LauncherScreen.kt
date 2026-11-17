package de.liquidlauncher.launcher.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.core.graphics.drawable.toBitmap
import de.liquidlauncher.launcher.data.AppEntry
import de.liquidlauncher.launcher.data.AppRepository
import de.liquidlauncher.launcher.theme.LiquidColors
import de.liquidlauncher.launcher.theme.LiquidDimens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "LauncherScreen"

/** Startoberfläche: Verlaufshintergrund und Raster aller startbaren Apps. */
@Composable
fun LauncherScreen() {
    val context = LocalContext.current
    val apps by produceState(initialValue = emptyList<AppEntry>()) {
        value = withContext(Dispatchers.IO) { AppRepository.loadLaunchableApps(context) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        LiquidColors.WallpaperTop,
                        LiquidColors.WallpaperMiddle,
                        LiquidColors.WallpaperBottom,
                    ),
                ),
            ),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(LiquidDimens.GridColumns),
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(apps, key = { it.packageName }) { app ->
                AppTile(
                    app = app,
                    onClick = { launch(context, app) },
                )
            }
        }
    }
}

@Composable
private fun AppTile(app: AppEntry, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppIcon(packageName = app.packageName)
        BasicText(
            text = app.label,
            modifier = Modifier.padding(top = 6.dp),
            style = TextStyle(
                color = LiquidColors.TextPrimary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun AppIcon(packageName: String) {
    val context = LocalContext.current
    val bitmap: ImageBitmap? by produceState<ImageBitmap?>(initialValue = null, packageName) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                context.packageManager
                    .getApplicationIcon(packageName)
                    .toBitmap(LiquidDimens.IconPixelSize, LiquidDimens.IconPixelSize)
                    .asImageBitmap()
            }.getOrNull()
        }
    }

    Box(
        modifier = Modifier
            .size(LiquidDimens.IconSizeDp.dp)
            .clip(RoundedCornerShape(LiquidDimens.IconCornerRadiusDp.dp))
            .background(LiquidColors.IconTileFill),
        contentAlignment = Alignment.Center,
    ) {
        bitmap?.let { image ->
            Image(
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

private fun launch(context: android.content.Context, app: AppEntry) {
    val intent = AppRepository.launchIntent(context, app.packageName)
    if (intent == null) {
        Log.w(TAG, "Keine Startaktivität für ${app.packageName}")
        return
    }
    runCatching { context.startActivity(intent) }
        .onFailure { Log.w(TAG, "Start von ${app.packageName} fehlgeschlagen", it) }
}
