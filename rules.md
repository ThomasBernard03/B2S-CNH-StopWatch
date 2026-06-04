# Project Rules

## 1. Comments

All comments must be written in English.

## 2. One Class Per File

Each file must contain exactly one top-level class or interface. Avoid multiple public classes in a single file.

## 3. Clean Architecture (Mobile)

The mobile codebase must follow Clean Architecture principles with a clear separation of concerns across layers:

- **Domain layer** — Contains business logic, use cases, and repository interfaces. No framework dependencies.
- **Data layer** — Implements repository interfaces from the domain layer. Handles data sources (local, remote).
- **Presentation layer** — Contains UI logic (ViewModels, Screens, Composables). Depends on the domain layer via use cases.

Dependencies must point inward: Presentation → Domain ← Data.

## 4. Composable Previews

Every `@Composable` function must have a corresponding `@Preview` function in the same file. This ensures all UI components are previewable in Android Studio without running the app.

## 5. Theme-Aware Styling

Do not hardcode colors or typography values. Always reference the Material 3 theme:

- Use `MaterialTheme.colorScheme.*` for colors
- Use `MaterialTheme.typography.*` for text styles

Never use raw color values (e.g., `Color.Red`, `0xFF000000`) or raw text styles directly in Composables.
