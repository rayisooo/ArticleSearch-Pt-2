package com.codepath.articlesearch

class ArticleApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
    parsedJson.response?.docs?.let { list ->
        (application as ArticleApplication).db.articleDao().deleteAll()
        (application as ArticleApplication).db.articleDao().insertAll(TODO)
    }

    parsedJson.response?.docs?.let { list ->
        (application as ArticleApplication).db.articleDao().deleteAll()
        (application as ArticleApplication).db.articleDao().insertAll(list.map {
            ArticleEntity(
                headline = it.headline?.main,
                articleAbstract = it.abstract,
                byline = it.byline?.original,
                mediaImageUrl = it.mediaImageUrl
            )
        })
    }

    parsedJson.response?.docs?.let { list ->
        lifecycleScope.launch(IO) {
            (application as ArticleApplication).db.articleDao().deleteAll()
            (application as ArticleApplication).db.articleDao().insertAll(list.map {
                ArticleEntity(
                    headline = it.headline?.main,
                    articleAbstract = it.abstract,
                    byline = it.byline?.original,
                    mediaImageUrl = it.mediaImageUrl
                )
            })
        }
    }

    lifecycleScope.launch {
        (application as ArticleApplication).db.articleDao().getAll().collect { databaseList ->
            databaseList.map { entity ->
                DisplayArticle(
                    entity.headline,
                    entity.articleAbstract,
                    entity.byline,
                    entity.mediaImageUrl
                )
            }.also { mappedList ->
                articles.clear()
                articles.addAll(mappedList)
                articleAdapter.notifyDataSetChanged()
            }
        }
    }
}