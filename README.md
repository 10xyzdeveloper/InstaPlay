# Instagram-Type Feed App

A modern Android application built with Jetpack Compose following Clean Architecture principles and best practices.

## 📋 Features

- **Feed Screen**: Infinite scrollable feed of posts with images and captions
- **Post Detail Screen**: Detailed view of individual posts
- **Like/Unlike Posts**: Toggle like status with real-time updates
- **Paging 3**: Efficient pagination for loading posts
- **Mock Data**: In-memory data generation for testing

## 🏗️ Architecture

This project follows **Clean Architecture** with **MVVM** pattern and **Unidirectional Data Flow**.

### Layer Structure

```
┌─────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                       │
│  • UI (Jetpack Compose)                                     │
│  • ViewModels                                               │
│  • UI States                                                │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                            │
│  • Use Cases (Business Logic)                               │
│  • Domain Models                                            │
│  • Repository Interfaces                                    │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                       DATA LAYER                             │
│  • Repository Implementations                               │
│  • Data Sources                                             │
│  • Data Models                                              │
│  • Paging Sources                                           │
└─────────────────────────────────────────────────────────────┘
```

## 📦 Package Structure

```
com.house.playtest/
├── data/
│   ├── model/           # Data entities
│   ├── source/          # Data sources (Mock)
│   ├── repository/      # Repository implementations
│   └── paging/          # Paging 3 sources
├── domain/
│   ├── model/           # Domain models
│   ├── repository/      # Repository interfaces
│   └── usecase/         # Use cases
├── presentation/
│   ├── feed/            # Feed screen
│   ├── detail/          # Detail screen
│   └── navigation/      # Navigation setup
├── di/                  # Dependency Injection
└── ui/theme/            # Theme & styling
```

## 🛠️ Tech Stack

### Core
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Coroutines & Flow** - Asynchronous programming

### Architecture Components
- **ViewModel** - UI state management
- **Navigation Compose** - Navigation between screens
- **Paging 3** - Efficient pagination
- **StateFlow** - Reactive state management

### Dependency Injection
- **Dagger Hilt** - Dependency injection framework

### Image Loading
- **Coil 3** - Async image loading

### Testing
- **JUnit** - Unit testing framework
- **MockK** - Mocking library
- **Turbine** - Flow testing
- **Coroutines Test** - Testing coroutines

## 🎯 Key Components

### Use Cases
- `GetPostsUseCase` - Fetches paginated posts
- `GetPostByIdUseCase` - Fetches single post by ID
- `ToggleLikeUseCase` - Toggles like status for a post

### Screens
1. **Feed Screen**
   - Displays infinite scrollable list of posts
   - Pull to refresh
   - Like/unlike functionality
   - Click to view details

2. **Post Detail Screen**
   - Full post view
   - Larger image
   - Author information
   - Like/unlike functionality
   - Timestamp

## 🧪 Testing

### Running Unit Tests

```bash
./gradlew test
```

### Test Coverage
- ✅ FeedViewModel tests
- ✅ PostDetailViewModel tests
- Tests cover success, error, and loading states
- Tests verify like/unlike functionality

### Key Test Files
- `FeedViewModelTest.kt` - Tests for feed screen ViewModel
- `PostDetailViewModelTest.kt` - Tests for detail screen ViewModel

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 11 or higher
- Android SDK 24+

### Building the Project

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on emulator or device

### Running the App

```bash
./gradlew installDebug
```

Or use Android Studio's Run button.

## 📱 App Flow

1. App opens to **Feed Screen**
2. Posts load automatically with pagination
3. Scroll down to load more posts
4. Tap heart icon to like/unlike a post
5. Tap on post to view **Post Detail Screen**
6. View full post details
7. Like/unlike from detail screen
8. Use back button to return to feed

## 🎨 UI/UX Features

- **Material Design 3** - Modern UI components
- **Edge-to-edge** display
- **Loading states** - Circular progress indicators
- **Error handling** - Error messages with retry
- **Smooth animations** - Natural transitions
- **Responsive design** - Adapts to different screen sizes

## 🔄 Data Flow

### Like Action Flow
```
User taps Like
    ↓
UI calls ViewModel.toggleLike()
    ↓
ViewModel calls ToggleLikeUseCase
    ↓
UseCase calls Repository.toggleLike()
    ↓
Repository updates DataSource
    ↓
DataSource updates in-memory state
    ↓
Updated data flows back to UI
    ↓
UI updates with new like state
```

## 🔧 Configuration

### Mock Data
- **Total Posts**: 100 (configurable in `MockPostDataSource.kt`)
- **Page Size**: 10 posts per page
- **Image Sources**: Lorem Picsum (10 predefined URLs)
- **Captions**: 15 random caption templates
- **Authors**: 12 predefined author names

### Customization
- Modify image URLs in `MockPostDataSource.kt`
- Adjust page size in `GetPostsUseCase.kt`
- Add more caption templates in `MockPostDataSource.kt`

## 📝 Best Practices Implemented

✅ Clean Architecture - Clear separation of concerns  
✅ SOLID Principles - Single responsibility, dependency inversion  
✅ Unidirectional Data Flow - Predictable state management  
✅ Immutable State - Thread-safe state handling  
✅ Repository Pattern - Abstracted data layer  
✅ Use Cases - Single-purpose business logic  
✅ Dependency Injection - Testable, loosely coupled code  
✅ Coroutines - Non-blocking async operations  
✅ StateFlow/Flow - Reactive programming  
✅ Unit Tests - ViewModel testing with mocks  

## 🔮 Future Enhancements

- Add Room database for persistence
- Implement pull-to-refresh
- Add comments functionality
- Implement user profiles
- Add image upload
- Implement search functionality
- Add animations and transitions
- Implement dark mode toggle
- Add offline support
- Implement real backend integration

## 📄 License

This is a sample project for learning and demonstration purposes.

## 👨‍💻 Author

Built with ❤️ using Jetpack Compose and Clean Architecture principles.

