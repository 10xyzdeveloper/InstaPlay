# Architecture Documentation

## Overview

This project implements **Clean Architecture** with **MVVM** pattern, ensuring maintainability, testability, and scalability.

## Architecture Layers

### 1. Presentation Layer (`presentation/`)

**Responsibility**: Handle UI and user interactions

**Components**:
- **Composables**: UI components built with Jetpack Compose
- **ViewModels**: Manage UI state and handle user actions
- **UI States**: Sealed classes/data classes representing screen states
- **Navigation**: Navigation graph and screen definitions

**Key Classes**:
```kotlin
FeedViewModel          // Manages feed screen state
FeedScreen            // Feed UI composable
PostDetailViewModel   // Manages detail screen state
PostDetailScreen      // Detail UI composable
NavGraph              // Navigation configuration
```

**Dependencies**: Domain layer only (no direct data layer access)

---

### 2. Domain Layer (`domain/`)

**Responsibility**: Business logic and rules

**Components**:
- **Models**: Domain entities (pure Kotlin classes)
- **Repository Interfaces**: Contracts for data operations
- **Use Cases**: Single-responsibility business operations

**Key Classes**:
```kotlin
Post                  // Domain model
PostRepository        // Repository contract
GetPostsUseCase       // Fetch paginated posts
GetPostByIdUseCase    // Fetch single post
ToggleLikeUseCase     // Like/unlike post
```

**Dependencies**: None (pure Kotlin, no Android/Framework dependencies)

---

### 3. Data Layer (`data/`)

**Responsibility**: Data management and persistence

**Components**:
- **Repository Implementations**: Implement domain contracts
- **Data Sources**: Abstract data access (API, Database, Cache)
- **Data Models**: Data entities with serialization
- **Mappers**: Convert between data and domain models
- **Paging Sources**: Paging 3 integration

**Key Classes**:
```kotlin
PostEntity            // Data model
PostDataSource        // Data source interface
MockPostDataSource    // In-memory implementation
PostRepositoryImpl    // Repository implementation
PostPagingSource      // Paging 3 source
```

**Dependencies**: Domain layer interfaces

---

## Dependency Injection (`di/`)

**Responsibility**: Provide dependencies throughout the app

**Components**:
```kotlin
AppModule             // App-level dependencies
  - Binds PostDataSource
  - Binds PostRepository
```

**Hilt Scopes**:
- `@Singleton` - Data sources and repositories
- `@ViewModelScoped` - Use cases (automatically handled)

---

## Data Flow

### Reading Data (Feed Screen)

```
┌─────────────────┐
│   FeedScreen    │ User opens screen
└────────┬────────┘
         ↓
┌─────────────────┐
│  FeedViewModel  │ Calls getPostsUseCase()
└────────┬────────┘
         ↓
┌─────────────────┐
│ GetPostsUseCase │ Creates Pager with PostPagingSource
└────────┬────────┘
         ↓
┌─────────────────┐
│PostPagingSource │ Calls repository.getPosts()
└────────┬────────┘
         ↓
┌─────────────────┐
│PostRepositoryImpl│ Calls dataSource.getPosts()
└────────┬────────┘
         ↓
┌─────────────────┐
│MockPostDataSource│ Returns mock data
└────────┬────────┘
         ↓
Flow<PagingData<Post>>
         ↓
┌─────────────────┐
│   FeedScreen    │ Renders posts in LazyColumn
└─────────────────┘
```

### Writing Data (Like Post)

```
┌─────────────────┐
│    PostItem     │ User taps like button
└────────┬────────┘
         ↓
┌─────────────────┐
│  FeedViewModel  │ Calls toggleLike(postId)
└────────┬────────┘
         ↓
┌─────────────────┐
│ToggleLikeUseCase│ Calls repository.toggleLike()
└────────┬────────┘
         ↓
┌─────────────────┐
│PostRepositoryImpl│ Calls dataSource.toggleLike()
└────────┬────────┘
         ↓
┌─────────────────┐
│MockPostDataSource│ Updates in-memory post state
└────────┬────────┘
         ↓
     Result<Post>
         ↓
┌─────────────────┐
│  FeedViewModel  │ Paging 3 auto-refreshes
└────────┬────────┘
         ↓
┌─────────────────┐
│   FeedScreen    │ UI updates with new like state
└─────────────────┘
```

---

## State Management

### UI State Pattern

Each screen has a sealed class representing its state:

```kotlin
sealed class FeedUiState {
    object Loading : FeedUiState()
    object Success : FeedUiState()
    data class Error(val message: String) : FeedUiState()
}
```

**Benefits**:
- Type-safe state representation
- Exhaustive when expressions
- Clear state transitions
- Easy to test

### StateFlow Usage

```kotlin
private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
val uiState: StateFlow<UiState> = _uiState.asStateFlow()
```

**Benefits**:
- Lifecycle-aware
- Survives configuration changes
- Emits latest value to new subscribers
- Thread-safe

---

## Navigation Architecture

### Type-Safe Navigation

```kotlin
sealed class Screen(val route: String) {
    object Feed : Screen("feed")
    object PostDetail : Screen("post_detail/{postId}") {
        fun createRoute(postId: String) = "post_detail/$postId"
    }
}
```

**Benefits**:
- Compile-time safety
- Centralized route definitions
- Type-safe argument passing
- Easy refactoring

---

## Testing Strategy

### Unit Tests

**What We Test**:
- ✅ ViewModels with mocked dependencies
- ✅ State transitions
- ✅ User actions (like, retry)
- ✅ Error handling

**What We Don't Test**:
- ❌ Composables (use instrumented tests)
- ❌ Navigation (use instrumented tests)
- ❌ UI rendering (use screenshot tests)

### Test Structure

```kotlin
@Test
fun `descriptive test name in backticks`() = runTest {
    // Given - Setup
    coEvery { useCase() } returns expected
    
    // When - Action
    viewModel.performAction()
    
    // Then - Assertion
    assertEquals(expected, viewModel.state.value)
}
```

---

## Design Patterns

### Repository Pattern
Abstracts data sources, allowing easy switching between implementations.

### Use Case Pattern
Encapsulates single business operation, promoting reusability and testing.

### Mapper Pattern
Converts between layers, maintaining separation of concerns.

### Observer Pattern
StateFlow/Flow for reactive data streams.

### Dependency Injection Pattern
Hilt provides dependencies, improving testability.

---

## SOLID Principles

### Single Responsibility
- Each Use Case does one thing
- Each ViewModel manages one screen
- Each Repository handles one entity type

### Open/Closed
- Interfaces allow extension without modification
- Sealed classes for controlled hierarchies

### Liskov Substitution
- Mock implementations can replace real ones
- Interface-based dependencies

### Interface Segregation
- Small, focused interfaces
- Clients don't depend on unused methods

### Dependency Inversion
- High-level modules depend on abstractions
- Domain layer is independent of data layer

---

## Best Practices

### ✅ DO

- Use immutable data classes for state
- Expose StateFlow, never MutableStateFlow
- Use suspend functions for async operations
- Keep ViewModels platform-independent
- Write tests for ViewModels
- Use sealed classes for state representation
- Follow single responsibility principle
- Use dependency injection
- Separate domain and data models

### ❌ DON'T

- Pass Context to ViewModels
- Use LiveData (use StateFlow instead)
- Put business logic in Composables
- Access data layer from ViewModels directly
- Use mutable state in Composables
- Hardcode dependencies
- Mix UI and business logic
- Skip error handling
- Forget to handle loading states

---

## Performance Considerations

### Paging 3
- Loads data incrementally
- Reduces memory footprint
- Improves scroll performance
- Automatic retry on failure

### Coroutines
- Non-blocking operations
- Structured concurrency
- Automatic cancellation
- Efficient threading

### StateFlow
- Only emits distinct values
- Conflates rapid updates
- Lifecycle-aware collection
- Survives configuration changes

### Compose
- Smart recomposition
- Skips unchanged UI
- Efficient rendering
- Lazy loading with LazyColumn

---

## Scalability

### Adding New Features

1. **New Screen**:
   - Create ViewModel + UiState in `presentation/`
   - Create Composable screen
   - Add to NavGraph
   - Write tests

2. **New Data Source**:
   - Create interface in `data/source/`
   - Create implementation
   - Bind in Hilt module
   - Update repository

3. **New Business Logic**:
   - Create Use Case in `domain/usecase/`
   - Inject into ViewModel
   - Call from UI
   - Write tests

---

## Migration Path

### From Mock to Real API

1. Create `RemotePostDataSource implements PostDataSource`
2. Add Retrofit/Ktor dependencies
3. Create API service and DTOs
4. Update Hilt binding
5. No changes needed in domain or presentation!

### Adding Database

1. Create `LocalPostDataSource implements PostDataSource`
2. Add Room dependencies
3. Create Entity, DAO, Database
4. Update repository to use both remote and local
5. Implement caching strategy

---

## Conclusion

This architecture provides:
- ✅ Clear separation of concerns
- ✅ High testability
- ✅ Easy maintenance
- ✅ Scalability
- ✅ Framework independence in domain layer
- ✅ Flexibility to change implementations
- ✅ Team-friendly structure

The architecture is production-ready and follows industry best practices for modern Android development.

