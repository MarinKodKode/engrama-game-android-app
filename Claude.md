# Engrama Android: instrucciones para Claude Code

## Qué es este proyecto
Port a Android de Engrama, app de entrenamiento cognitivo publicada en iOS ("deja una marca en tu cerebro": recuperación activa bajo presión). El port ya existe de forma parcial (~20% de lo que hay en iOS), en Jetpack Compose + Hilt. Su estado exacto se documenta en la auditoría inicial.

## Fuente de verdad
- El repo iOS es la fuente de verdad de lógica, datos, formas de documento de Firestore y diseño. Es SOLO LECTURA: nunca modifiques nada en él.
- Ambos repos están bajo ~/Documents/Github/LeetCode/
  - Repo iOS: <ruta absoluta, se completa tras la auditoría>
  - Repo Android: <ruta absoluta, se completa tras la auditoría>
- Ante cualquier duda de comportamiento, lee la implementación iOS antes de decidir. No inventes lógica.

## Reglas de trabajo (obligatorias)
1. Plan antes de código: en toda tarea, primero muestra el plan de archivos (crear/modificar/borrar) y tus dudas. No implementes hasta que yo lo apruebe explícitamente.
2. Por fases: no avances a la siguiente fase sin que la actual compile limpio (./gradlew assembleDebug) y yo la valide.
3. Git: NUNCA hagas git add, commit, push, stash, rebase, merge, reset ni cambies de rama. Deja los cambios sin stagear para que yo los revise. Solo puedes usar git status, git diff, git log y git branch.
4. Firebase: NUNCA hagas deploy (reglas, índices ni nada) ni uses Firebase CLI contra el proyecto. Si un cambio requiere modificar firestore.rules o firestore.indexes.json, muéstrame el diff propuesto y detente.
5. Cambios no planeados: si en el camino necesitas tocar algo fuera del plan aprobado (refactor, dependencia, archivo extra), detente y pídeme aprobación. Al final de cada tarea, lista explícitamente cualquier desviación del plan.
6. Dependencias: no agregues ni actualices dependencias ni versiones de Gradle/AGP/Kotlin/Compose sin aprobación.
7. No asumas el estado del repo: si no lo verificaste, dilo.
8. Idioma: responde en español, directo, sin explicar lo básico.

## Diseño
- No inventes estilos. Reusa los componentes, tokens de color y tipografía ya existentes en el proyecto.
- Fuente Fredoka (res/font) y paleta Engrama, equivalentes a los de iOS.
- Si falta un componente, constrúyelo con el mismo lenguaje visual de iOS y avísame.
- Nada de colores, tamaños ni tipografías hardcodeados en pantallas: todo sale del tema.

## Stack y mapeo iOS → Android
- SwiftUI → Jetpack Compose
- MVVM con @Observable → ViewModel + StateFlow (UDF)
- SwiftData → Room
- Inyección → Hilt
- Firebase Auth/Firestore → SDKs Android (Firebase BOM). Google Sign-In con Credential Manager, usando el web client ID del proyecto Firebase.
- Háptico de error → View.performHapticFeedback(HapticFeedbackConstants.REJECT) en API 30+, con fallback a Vibrator
- ImageRenderer + ShareSheet → rememberGraphicsLayer() + toImageBitmap() + FileProvider + Intent.ACTION_SEND
- Deep link engrama://deck/{shareId} → intent-filter (App Links en fase posterior)
- Radar chart → Canvas custom en Compose
- CHAEA.json, VAK.json, LearningInterpretations.json → assets, copiados del repo iOS sin alterar; mismo scoring y baremo

## Firestore (compatibilidad entre plataformas, crítico)
- Mismo proyecto Firebase que iOS: engrama-35880. La fuente de verdad del contrato es el CÓDIGO iOS (rama: <main|develop, por confirmar>), no este archivo. Si hay contradicción, gana iOS y me lo reportas.
- Colecciones que usa iOS:
  - users/{uid} (documento raíz de progreso, escritura con merge)
  - users/{uid}/sessions/{uuid}
  - users/{uid}/decks/{id} y /cards/{id}
  - users/{uid}/generatedDecks (+cards): legado Gemini, solo lectura de compatibilidad
  - users/{uid}/progress/{cardId}: estado SM2
  - users/{uid}/importedDecks/{deckId} (+cards)
  - communityDecks (+cards), communityCards
  - usernames/{username} → { uid } (solo create; username normalizado trim + lowercase)
  - deckShareLinks/{shareId}
  - deckAssignments/{id} (cardsSnapshot embebido, máximo 100 tarjetas; progress.status inicial 'pending'; las actualizaciones solo tocan progress.* con rutas con punto; sin soft-delete)
  - users/{uid}/chaeaAttempts, vakAttempts (solo en iOS develop)
- Persistencia local: settings y perfil en DataStore (equivalente a UserDefaults); sesiones, mazos y tarjetas en Room. DTOs de Firestore separados de las entidades Room.
- DTOs: data classes con default en TODOS los campos.
- Booleanos isX (isDeleted, isActive, hasActiveShares…): @get:PropertyName / @set:PropertyName con el nombre exacto de iOS.
- Enums: se serializan con el rawValue EXACTO de iOS ("División Plutón", "math", "flashcards_quiz", "substract"…). Prohibido usar enum.name. Los valores desconocidos se manejan de forma explícita y con log, nunca con un fallback silencioso.
- Fechas: Timestamp en Firestore.
- SM2: idéntico a Features/Flashcards/Algorithms/SM2.swift (EF se actualiza también en fallo, mínimo 1.3, redondeo half away from zero, días en la zona horaria del dispositivo).
- No uses índices compuestos que no existan en firestore.indexes.json.

## Excluido o pendiente
- Generación de tarjetas con Gemini (GenerationPanelView en iOS): EXCLUIDA. No portar hasta nuevo aviso.
- Pantalla de resultados del Perfil de Aprendizaje: en rediseño en iOS. No portar la versión actual; esperar la final.
- Generación con Gemini: eliminada en iOS. No portar. Mantener CardSource.geminiAPI ("geminiAPI") solo para decodificar datos existentes.

## Formato de tus reportes al cerrar cada tarea
- Qué se hizo (archivos con ruta).
- Resultado de ./gradlew assembleDebug y tests.
- Desviaciones del plan aprobado (o "ninguna").
- Pendientes y preguntas abiertas.
