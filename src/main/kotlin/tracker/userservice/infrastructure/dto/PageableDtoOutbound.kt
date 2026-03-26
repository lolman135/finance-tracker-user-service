package tracker.userservice.infrastructure.dto

data class PageableDtoOutbound<T>(
    val items: List<T>,
    val metadata: PaginationMetadata
)