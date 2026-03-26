package tracker.userservice.infrastructure.dto

data class PaginationMetadata(
    val totalElements: Long,
    val currentPage: Int,
    val pageSize: Int
) {
}