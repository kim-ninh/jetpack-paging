package com.android.example.paging.pagingwithnetwork.reddit.repository.inDb

import android.annotation.SuppressLint
import androidx.paging.ItemKeyedDataSource
import androidx.room.InvalidationTracker
import androidx.room.InvalidationTracker.Observer
import com.android.example.paging.pagingwithnetwork.reddit.db.RedditDb
import com.android.example.paging.pagingwithnetwork.reddit.vo.RedditPost

@SuppressLint("RestrictedApi")
class DbItemKeyedSubredditDataSource(
        private val subredditName: String,
        private val db: RedditDb)
    : ItemKeyedDataSource<String, RedditPost>() {

    private val observer: Observer =
            object : InvalidationTracker.Observer("posts") {
                override fun onInvalidated(tables: MutableSet<String>) {
                    invalidate()
                }
            }

    init {
        db.invalidationTracker.addWeakObserver(observer)
    }

    override fun isInvalid(): Boolean {
        db.invalidationTracker.refreshVersionsSync()
        return super.isInvalid()
    }

    override fun getKey(item: RedditPost): String = item.name

    override fun loadInitial(
            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<RedditPost>
    ) {

        val initialKey = params.requestedInitialKey
        val redditPosts = if (initialKey != null){
            db.posts()
                    .postsBySubreddit(subredditName, params.requestedLoadSize, initialKey)
        }else{
            db.posts()
                    .postsBySubreddit(subredditName, params.requestedLoadSize)
        }

        callback.onResult(redditPosts)
    }

    override fun loadAfter(
            params: LoadParams<String>,
            callback: LoadCallback<RedditPost>
    ) {
        val redditPosts = db
                .posts()
                .postsBySubreddit(subredditName, params.requestedLoadSize, params.key)
        callback.onResult(redditPosts)
    }

    override fun loadBefore(
            params: LoadParams<String>,
            callback: LoadCallback<RedditPost>
    ) {
        // ignore
    }
}