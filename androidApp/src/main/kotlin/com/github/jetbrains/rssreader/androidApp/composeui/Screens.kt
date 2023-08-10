package com.github.jetbrains.rssreader.androidApp.composeui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.jetbrains.rssreader.androidApp.home.TabItem
import com.github.jetbrains.rssreader.androidApp.home.Tabs
import com.github.jetbrains.rssreader.androidApp.home.TabsContent
import com.github.jetbrains.rssreader.app.FeedAction
import com.github.jetbrains.rssreader.app.FeedStore
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainScreen : Screen, KoinComponent {
    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val store: FeedStore by inject()
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val state by store.observeState().collectAsState()
        val refreshState = rememberPullRefreshState(
            refreshing = state.progress,
            onRefresh = { store.dispatch(FeedAction.Refresh(true)) }
        )
        val tabs = listOf(
            TabItem.Music(refreshState, store, context, navigator, state),
            TabItem.Movies,
            TabItem.Books
        )
        val pagerState = androidx.compose.foundation.pager.rememberPagerState()
        LaunchedEffect(Unit) {
            store.dispatch(FeedAction.Refresh(false))
        }

        Column {
            Tabs(tabs = tabs, pagerState = pagerState)
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }


    class FeedListScreen : Screen, KoinComponent {
        @Composable
        override fun Content() {
            val store: FeedStore by inject()
            FeedList(store = store)
        }
    }
}
/*class TabHomeScreen : Screen, KoinComponent {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {

        val tabs = listOf(TabItem.Music, TabItem.Movies, TabItem.Books)
        val pagerState = androidx.compose.foundation.pager.rememberPagerState()
        Column {
            Tabs(tabs = tabs, pagerState = pagerState)
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }
}*/



