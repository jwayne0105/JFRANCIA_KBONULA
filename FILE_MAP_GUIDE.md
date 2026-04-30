
## 1) App Flow (Menu -> Settings -> Game -> Game Over)
- `src/com/progmemorymatch/MemoryMatchApp.java`
- Role:
  - Main window
  - Screen switching
  - Start campaign / next stage flow

## 2) Main Menu UI
- `src/com/progmemorymatch/ui/MainMenuPanel.java`
- Role:
  - Start Campaign button
  - Settings / Mute / Exit
  - Title and campaign intro text

## 3) Campaign Gameplay (Core Logic)
- `src/com/progmemorymatch/ui/GamePanel.java`
- Role:
  - Memory match mechanics
  - Stage progression (padami nang padami cards)
  - Initial reveal preview (pinapakita muna lahat ng cards)
  - Move limit and countdown timer per stage
  - Time penalty on wrong guess
  - Lose condition trigger for Game Over
  - Timer, score, move counter
  - Learning panel text per matched language

## 4) Stage Result / Game Over
- `src/com/progmemorymatch/ui/GameOverPanel.java`
- `src/com/progmemorymatch/model/GameResult.java`
- Role:
  - Stage summary
  - Win/lose state handling
  - Rank display
  - Dynamic next action (`Next Level`, `Retry Stage`, or `Restart Campaign`)

## 5) Deck and Data Models
- `src/com/progmemorymatch/model/DeckFactory.java`
- `src/com/progmemorymatch/model/CardModel.java`
- `src/com/progmemorymatch/model/LanguageInfo.java`
- `src/com/progmemorymatch/model/GameSettings.java`
- Role:
  - Card deck generation and shuffle
  - Language facts
  - Start tier / theme / audio / timing settings

## 6) Audio (May Sound)
- `src/com/progmemorymatch/audio/SoundManager.java`
- Included sounds:
  - Background loop
  - Match SFX
  - Mismatch SFX
  - Button click SFX

## 7) UI Styling
- `src/com/progmemorymatch/ui/ThreeDButton.java`
- `src/com/progmemorymatch/ui/CardButton.java`
- `src/com/progmemorymatch/ui/ThemePalette.java`
- `src/com/progmemorymatch/ui/GradientPanel.java`
- Role:
  - 3D/glossy button style
  - Card visuals and badges
  - Brown arcade-like theme set

## 8) Submission Files
- `README.md`
- `screenshots/`
- `.gitignore`

## Suggested Task Split (2 members)
- Member A:
  - `ui/GamePanel.java`
  - `model/DeckFactory.java`
  - `model/CardModel.java`
  - `model/GameResult.java`
- Member B:
  - `MemoryMatchApp.java`
  - `ui/MainMenuPanel.java`
  - `ui/SettingsPanel.java`
  - `ui/GameOverPanel.java`
  - `audio/SoundManager.java`
  - `ui/ThreeDButton.java`
  - `ui/ThemePalette.java`

## Reminder For Commit Quality
- Avoid vague messages (`update`, `fix`, `test`).
- Example:
  - `Add stage-preview reveal phase and lock board until preview ends`
  - `Implement campaign stage progression and dynamic game-over next action`
