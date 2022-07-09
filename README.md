# Laplacity

Описание и техническое задание [здесь](https://github.com/timattt/Steel-unicorn/blob/master/About/Laplacity.md)

# Build

1. Устанавливаем Android SDK. Для этого потребуется установить Android studio   
2. Устанавливаем JDK [отсюда](https://adoptium.net/)   
3. Устанавливаем gradle   
4. Клонируем репозиторий
5. Теперь можно импортировать gradle-проект в ваш любимый IDE
6. Или можно все делать из консоли. Для этого есть следующие полезные команды:

* Запуск на компьютере

```
gradlew desktop:run
```

* Сборка устоновочных **apk** файлов для Андройда.

```
gradlew android:installDebug
```

* Запуск сборки Android на эмуляторе, либо на подключенном устройстве

```
gradlew android:run
```
