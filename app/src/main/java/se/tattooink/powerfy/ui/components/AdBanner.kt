package se.tattooink.powerfy.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import se.tattooink.powerfy.R

private data class AdBanner(val drawableRes: Int, val aspectRatio: Float)

// Aspect ratio matches each source image's real pixel dimensions so the
// banner is scaled proportionally without cropping or letterboxing.
private val adBanners = listOf(
    AdBanner(R.drawable.ba1, aspectRatio = 792f / 420f),
    AdBanner(R.drawable.ba2, aspectRatio = 1077f / 486f),
    AdBanner(R.drawable.ba3, aspectRatio = 792f / 420f),
    AdBanner(R.drawable.ba4, aspectRatio = 1077f / 486f)
)

private const val AUTO_SCROLL_DELAY_MS = 4500L
private const val SCROLL_ANIMATION_DURATION_MS = 900
private val BANNER_HEIGHT = 96.dp

// Pages are sized to roughly match a banner's own width (height * average
// aspect ratio) instead of the full screen width, so there's no leftover
// empty space inside a page pushing the next banner far away.
private val AVERAGE_ASPECT_RATIO = adBanners.map { it.aspectRatio }.average().toFloat()
private val PAGE_WIDTH = BANNER_HEIGHT * AVERAGE_ASPECT_RATIO

private const val VIRTUAL_PAGE_COUNT = Int.MAX_VALUE

@Composable
fun AdBannerRow(modifier: Modifier = Modifier) {
    val startPage = remember { (VIRTUAL_PAGE_COUNT / 2) - (VIRTUAL_PAGE_COUNT / 2) % adBanners.size }
    val pagerState = rememberPagerState(
        initialPage = startPage,
        pageCount = { VIRTUAL_PAGE_COUNT }
    )

    LaunchedEffect(pagerState) {
        while (true) {
            delay(AUTO_SCROLL_DELAY_MS)
            pagerState.animateScrollToPage(
                page = pagerState.currentPage + 1,
                animationSpec = tween(
                    durationMillis = SCROLL_ANIMATION_DURATION_MS,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(start = 0.dp, end = 72.dp),
            pageSpacing = 8.dp,
            pageSize = PageSize.Fixed(PAGE_WIDTH),
            modifier = Modifier
                .fillMaxWidth()
                .height(BANNER_HEIGHT)
        ) { page ->
            val banner = adBanners[page.mod(adBanners.size)]
            val pageOffset = pagerState.getOffsetDistanceInPages(page)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = banner.drawableRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(banner.aspectRatio)
                        .graphicsLayer {
                            val scale = 1f - (0.06f * pageOffset.coerceIn(-1f, 1f).let { if (it < 0) -it else it })
                            scaleX = scale
                            scaleY = scale
                        }
                        .alpha(1f - 0.25f * pageOffset.coerceIn(-1f, 1f).let { if (it < 0) -it else it })
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.height(8.dp)
        ) {
            repeat(adBanners.size) { index ->
                val isSelected = pagerState.currentPage.mod(adBanners.size) == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.Black else Color.LightGray)
                )
            }
        }
    }
}