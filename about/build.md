# Build

1. Устанавливаем Android SDK. Для этого потребуется установить Android studio   
2. Устанавливаем JDK [отсюда](https://adoptium.net/)   
3. Устанавливаем gradle   
4. Клонируем репозиторий
5. Теперь можно импортировать gradle-проект в ваш любимый IDE
6. Для сборки с подписью apk нужно установить переменные окружения: **ANDROID_KEYSTORE_ALIAS**, **ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD**, **ANDROID_KEYSTORE_PATH**, **ANDROID_KEYSTORE_PASSWORD**.
7. Еще нужно в папке android создать файл **version.properties** и в записать ```VERSION_CODE=31```, или любое другое число - это нужно для номера версии в google play.
8. Или можно все делать из консоли. Для этого есть следующие полезные команды:

* Запуск на компьютере

```
gradlew lwjgl3:run
```

* Сборка устоновочных **apk** файлов для Андройда.

```
gradlew android:installDebug
```

* Запуск сборки Android на эмуляторе, либо на подключенном устройстве

```
gradlew android:run
```
