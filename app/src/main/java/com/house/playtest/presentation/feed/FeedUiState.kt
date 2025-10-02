package com.house.playtest.presentation.feed

/**
 * UI state for the Feed screen.
 * 
 * Note: Post list loading/error states are handled by Paging 3's LoadState.
 * This class holds only screen-level state that's not related to pagination.
 */
data class FeedUiState(
    val snackbarMessage: String? = null,
    // Easy to extend in the future:
    // val selectedFilter: FilterType = FilterType.ALL,
    // val searchQuery: String = "",
    // val sortOrder: SortOrder = SortOrder.NEWEST_FIRST,
    // val showFilters: Boolean = false,
)

