# Bragi Movies Database

A modern Android application that provides information about movies using The Movie Database (TMDb)
API.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Building the App](#building-the-app)
- [Libraries Used](#libraries-used)
- [Testing](#testing)

## Introduction

Bragi Movies Database is an Android application that allows users to browse and explore movie
information powered
by [The Movie Database (TMDb) API](https://www.themoviedb.org/documentation/api).

## Features

- Browse popular movies
- Filter movies by genre

## Architecture

The application follows Clean Architecture principles with a modular approach:

```
com.bragi
├── app              # Application layer with MainActivity and app-level setup
├── core             # Core components shared across features
│   ├── data         # Base data layer components and network setup
│   ├── domain       # Base domain layer components (errors, results)
│   └── presentation # Base UI components and navigation
└── features         # Feature modules
    └── movies       # Movies feature
        ├── data     # Movie data sources and repositories
        ├── domain   # Movie domain models and use cases
        └── presentation # Movie UI components and view models
```

### Key architectural components:

- **Clean Architecture**: Separation of concerns with data, domain, and presentation layers
- **MVVM Pattern**: Used in the presentation layer with ViewModels
- **Dependency Injection**: Using Koin for providing dependencies
- **Jetpack Compose**: For modern, declarative UI
- **Kotlin Coroutines**: For asynchronous programming
- **Ktor Client**: For network requests

## Getting Started

### Prerequisites

- Android Studio (latest stable version recommended)
- JDK 11 or newer
- Android SDK with minimum API level 24

### Building the App

1. Clone the repository
2. Create a `local.properties` file in the root project directory if it doesn't exist
3. Add your TMDb API key to `local.properties`:
   ```
   API_KEY=your_api_key_here
   ```
4. Open the project in Android Studio
5. Sync project with Gradle files
6. Build and run the app:
   ```
   ./gradlew assembleDebug
   ```
   or use the Run button in Android Studio

## Libraries Used

### UI

- **Jetpack Compose**: Modern toolkit for building native UI
- **Navigation Compose**: For in-app navigation
- **Coil**: Image loading library for Compose

### Networking

- **Ktor Client**: HTTP client for making API requests
- **Kotlinx Serialization**: For JSON parsing

### Dependency Injection

- **Koin**: Lightweight dependency injection framework

### Logging

- **Timber**: Logging utility

### Testing

- **JUnit**: Unit testing framework
- **Mockito**: Mocking framework for testing
- **Kotlinx Coroutines Test**: For testing coroutines

## Testing

Run the tests using:

```
./gradlew test
```

For instrumentation tests:

```
./gradlew connectedAndroidTest
```