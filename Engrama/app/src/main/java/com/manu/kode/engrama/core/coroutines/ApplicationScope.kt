package com.manu.kode.engrama.core.coroutines

import javax.inject.Qualifier

/** CoroutineScope de aplicación para escrituras que deben sobrevivir al ViewModel. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope
