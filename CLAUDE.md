# Engrama Android: instrucciones para Claude Code

## Qué es este proyecto
Port a Android de Engrama, app de entrenamiento cognitivo publicada en iOS ("deja una marca en tu cerebro": recuperación activa bajo presión). El port está en Jetpack Compose + Hilt.

## Repos y fuente de verdad
- Repo iOS (SOLO LECTURA, fuente de verdad): /Users/mhernandezm/Documents/GitHub/LeetCode/engrama-game-ios-app
  - Referencia: rama origin/develop (contiene a main). Léela con git show / git archive al scratchpad; nunca cambies de rama en el repo iOS.
- Repo Android: /Users/mhernandezm/Documents/GitHub/LeetCode/engrama-game-android-app (proyecto Gradle en Engrama/)
- Ante cualquier duda de comportamiento, lee la implementación iOS antes de decidir. No inventes lógica.

## Notas del port (memoria entre sesiones)
- Carpeta: /Users/mhernandezm/Documents/GitHub/LeetCode/engrama-port-notes/ (fuera de los repos).
- Al iniciar una sesión, lee los archivos de esa carpeta en orden numérico antes de trabajar.
- Cada reporte de auditoría o cierre de fase se guarda ahí como NN-nombre.md, y en el chat solo va el resumen ejecutivo.

## Ramas Android (estado al 2026-10-08)
- main (3b6d409): esqueleto inicial; no compila. Auditado en 01-auditoria-main.md.
- develop: rama de trabajo real (Math, Word Type, Trivia, Analytics, Settings, datasets). Base para continuar.
- fase0/cp1-build-main (solo local): migración de build a AGP 9.4.1 / Gradle 9.7.1 / Kotlin 2.4.20 hecha sobre main. Es referencia; NO se mergea tal cual.

## Reglas de trabajo (obligatorias)
1. Plan antes de código: en toda tarea, primero muestra el plan de archivos (crear/modificar/borrar) y tus dudas. No implementes hasta que yo lo apruebe explícitamente.
2. Por fases: no avances a la siguiente fase o checkpoint sin que el actual compile limpio y yo lo valide.
3. Git: NUNCA hagas add, commit, push, fetch, pull, stash, rebase, merge, reset, cherry-pick ni cambies de rama. Deja los cambios sin stagear. Solo git de lectura (status, diff, log, show, branch, ls-files, ls-tree, merge-base, rev-list, for-each-ref, check-ignore).
4. Firebase: NUNCA hagas deploy ni uses Firebase CLI. Si un cambio requiere modificar firestore.rules o firestore.indexes.json, muéstrame el diff propuesto y detente.
5. Cambios no planeados: si necesitas tocar algo fuera del plan aprobado, detente y pide aprobación. Al cerrar cada tarea, lista explícitamente las desviaciones.
6. Dependencias: no agregues ni cambies versiones sin aprobación.
7. No asumas el estado del repo, de mis acciones ni de mis máquinas: verifícalo. Si un mensaje mío afirma algo que no cuadra con lo que ves, repórtalo antes de seguir.
8. Si algo no lo verificaste, márcalo como "no verificado".
9. Idioma: español, directo, sin explicar lo básico.

## Stack objetivo (Fase 0)
- Gradle 9.7.1 (con distributionSha256Sum), AGP 9.4.1 con Kotlin integrado, Kotlin 2.4.20, KSP 2.3.12 (sin kapt).
- compileSdk 37, targetSdk 36, minSdk 26, JVM 17.
- Compose BOM 2026.09.00, Navigation 2.10.2 (rutas tipadas, kotlinx-serialization 1.11.0), Lifecycle 2.11.0, Hilt 2.60.1, hilt-lifecycle-viewmodel-compose 1.4.0, Room 2.8.5, DataStore 1.2.1, core-ktx 1.19.1, activity-compose 1.13.0, google-services 4.5.0 (se aplica en la Fase 3).
- Todas las versiones en gradle/libs.versions.toml. Sin Gson.
- Detalle y verificación en 01-auditoria-main.md y en la tabla aprobada de la Fase 0.

## Arquitectura objetivo
- ViewModel + StateFlow con UiState inmutable por pantalla. Repositorios no expuestos desde los ViewModels.
- Settings y perfil en DataStore, una clave por campo, con el nombre de la ruta iOS (p. ej. math.substract.numberOfDigits).
- Sesiones (y luego mazos/tarjetas) en Room con exportSchema.
- Division, totalPoints, progress y pointsToNext se DERIVAN del Flow de sesiones; nunca se persisten ni se recalculan a mano.
- Enums persistidos con rawValue idéntico a iOS; fromRaw devuelve null y loguea lo desconocido. En DataStore se usa el default iOS del campo; en Room un raw desconocido lanza excepción (Room solo recibe datos validados; los mappers de entrada externa descartan con Log.e).

## Diseño
- No inventes estilos. Todo sale del tema: nada de colores, tamaños ni tipografías hardcodeados en pantallas.
- Fredoka (Regular, Medium, SemiBold, Bold) en res/font, copiada del repo iOS.
- Tokens de color derivados de los colores de sistema de iOS 17/18 (light/dark) y de los RGB inline de iOS. dynamicColor desactivado.
- Apariencia: el tema lee AppSettings.appearance (system/light/dark, default system). Sin selector en la UI hasta decidir si se restaura en iOS (se perdió en el merge c014bfb del repo iOS).
- Si falta un componente, constrúyelo con el lenguaje visual de iOS y avísame.

## Mapeo iOS → Android
- SwiftUI → Compose; @Observable → ViewModel + StateFlow; SwiftData/UserDefaults → Room/DataStore; inyección → Hilt.
- Firebase Auth/Firestore → SDKs Android; Google Sign-In con Credential Manager y el web client ID de engrama-35880.
- Háptico de error → View.performHapticFeedback(HapticFeedbackConstants.REJECT) en API 30+, fallback a Vibrator. (iOS: error = notificationOccurred(.warning), success = impacto light.)
- ImageRenderer + ShareSheet → rememberGraphicsLayer() + toImageBitmap() + FileProvider + ACTION_SEND.
- Deep link engrama://deck/{shareId} → intent-filter.
- Radar chart → Canvas custom.
- CHAEA.json, VAK.json, LearningInterpretations.json → assets copiados de iOS develop sin alterar.

## Firestore (compatibilidad entre plataformas, crítico)
- Mismo proyecto que iOS: engrama-35880. La app Android aún NO está registrada; hasta entonces Auth y Firestore no funcionan en runtime. applicationId: com.manu.kode.engrama.
- La fuente de verdad del contrato es el CÓDIGO iOS (origin/develop), no este archivo. Si hay contradicción, gana iOS y me lo reportas.
- Colecciones:
  - users/{uid} (documento raíz de progreso, escritura con merge)
  - users/{uid}/sessions/{uuid}
  - users/{uid}/decks/{id} y /cards/{id}
  - users/{uid}/generatedDecks (+cards): legado Gemini, solo lectura de compatibilidad
  - users/{uid}/progress/{cardId}: estado SM2
  - users/{uid}/importedDecks/{deckId} (+cards)
  - users/{uid}/chaeaAttempts, vakAttempts (solo en iOS develop)
  - communityDecks (+cards), communityCards
  - usernames/{username} → { uid } (solo create; username normalizado trim + lowercase)
  - deckShareLinks/{shareId} (update solo puede tocar isActive)
  - deckAssignments/{id} (cardsSnapshot embebido, máximo 100 tarjetas; progress.status inicial 'pending'; las actualizaciones solo tocan progress.* con rutas con punto; sin soft-delete)
- DTOs de Firestore: data classes con default en TODOS los campos, separadas de las entidades Room.
- Booleanos isX (isDeleted, isActive, hasActiveShares, hasActiveAssignments): @get:PropertyName / @set:PropertyName con el nombre exacto de iOS.
- Enums con el rawValue EXACTO de iOS ("División Plutón", "math", "flashcards_quiz", "substract"…). Prohibido usar enum.name.
- Fechas: Timestamp.
- SM2: idéntico a Features/Flashcards/Algorithms/SM2.swift (quality acotada 0…5; q<=2 reinicia; EF se actualiza también en fallo, mínimo 1.3; redondeo half away from zero; días en la zona horaria del dispositivo).
- No uses índices compuestos que no existan en firestore.indexes.json.

## Excluido o pendiente
- Generación con Gemini: eliminada en iOS. No portar. Mantener CardSource.geminiAPI ("geminiAPI") solo para decodificar datos existentes.
- Pantalla de resultados del Perfil de Aprendizaje: en rediseño en iOS. Esperar la versión final.

## Formato de cierre de cada tarea
- Qué se hizo (archivos con ruta).
- Resultado de assembleDebug y testDebugUnitTest (con conteo de tests).
- Desviaciones del plan aprobado (o "ninguna").
- No verificado.
- Pendientes y preguntas abiertas.
- Ruta del reporte guardado en engrama-port-notes/.
