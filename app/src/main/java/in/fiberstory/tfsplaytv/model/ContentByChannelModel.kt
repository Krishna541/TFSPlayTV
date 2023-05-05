package `in`.fiberstory.tfsplaytv.model

import `in`.fiberstory.tfsplaytv.model.Content

data class ContentByChannelModel(
    val `data`: ContentData,
    val status: String,
    val statusCode: String
)

data class ContentData(
    val content: List<Content>,
    val message: Any,
    val pageCount: Int
)