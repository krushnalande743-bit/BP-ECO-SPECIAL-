# BP-ECO-SPECIAL
A starter Fabric client mod project for Minecraft 26.2, styled with a dark charcoal + cyan UI.

## Included in this starter
- Right Shift opens a cyan-themed Click GUI
- Smooth fade/slide animation
- Toggleable HUD modules: Coordinates, FPS, Direction
- Settings are saved to `config/bp-eco-special.json`
- Clean project structure for adding more client-side modules

## Important
This is a **starter source project**, not a verified compiled JAR. It has not been built or tested in Minecraft in this environment.
The project intentionally does not include anti-cheat bypasses or hidden ESP/freecam behavior for public SMPs. Additions should be used only where the server owner explicitly permits them.

## Build
Requires JDK 25 and Gradle wrapper. Open the project in a compatible Java/Fabric development environment and run:
- Linux/macOS: `./gradlew build`
- Windows: `gradlew.bat build`

The output JAR, if the build succeeds, will be in `build/libs/`.
Check the current Fabric docs for the exact Minecraft/Fabric versions and dependencies before building:
https://docs.fabricmc.net/develop/getting-started/creating-a-project
