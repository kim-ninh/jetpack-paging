package com.android.example.paging.pagingwithnetwork.reddit.repository.inDb

import androidx.paging.DataSource
import com.android.example.paging.pagingwithnetwork.reddit.db.RedditDb
import com.android.example.paging.pagingwithnetwork.reddit.vo.RedditPost

class DbSubRedditDataSourceFactory(
        private val db: RedditDb,
        private val subredditName: String
): DataSource.Factory<String, RedditPost>() {

    override fun create(): DataSource<String, RedditPost> {
        val dataSource = DbItemKeyedSubredditDataSource(subredditName, db)
        return dataSource
    }
}