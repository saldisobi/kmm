package com.github.jetbrains.rssreader.androidApp.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.github.jetbrains.rssreader.androidApp.R
import com.github.jetbrains.rssreader.androidApp.composeui.MainFeed
import com.github.jetbrains.rssreader.app.FeedState
import com.github.jetbrains.rssreader.app.FeedStore

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int, var title: String, var screen: ComposableFun) {
    @OptIn(ExperimentalMaterialApi::class)
    data class Music @OptIn(ExperimentalMaterialApi::class) constructor(
        val refreshState: PullRefreshState,
        val store: FeedStore,
        val context: Context,
        val navigator: Navigator,
        val state: FeedState
    ) : TabItem(
        R.drawable.ic_add,
        "Music",
        { FeedConetnt(refreshState, store, context, navigator, state) })

    object Movies : TabItem(R.drawable.ic_add, "Movies", { MoviesScreen() })
    object Books : TabItem(R.drawable.ic_add, "Books", { BooksScreen() })
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun FeedConetnt(
    refreshState: PullRefreshState,
    store: FeedStore,
    context: Context,
    navigator: Navigator,
    state: FeedState
) {
    Box(modifier = Modifier.pullRefresh(refreshState)) {
        MainFeed(
            store = store,
            onPostClick = { post ->
                post.link?.let { url ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            },
            onEditClick = {
                //  navigator.push(FeedListScreen())
            }
        )
        PullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
            refreshing = state.progress,
            state = refreshState,
            scale = true //https://github.com/google/accompanist/issues/572
        )
    }
}
