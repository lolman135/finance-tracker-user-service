package tracker.userservice.domain

data class PageResponse<T>(
    val items: List<T>,
    val total: Long,
    val page: Int,
    val size: Int
)