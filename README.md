**[Вернуться](https://github.com/timattt/Steel-unicorn) к корневому репозиторию**

# Laplacity

* Описание и техническое задание [здесь](https://github.com/timattt/Steel-unicorn/blob/master/About/Laplacity.md)
* Дополнительные механики для новых 60 уровней смотрите [здесь](https://github.com/timattt/Steel-unicorn/blob/master/About/LaplacityMoreMechanics.md)

# Build

1. Устанавливаем Android SDK. Для этого потребуется установить Android studio   
2. Устанавливаем JDK [отсюда](https://adoptium.net/)   
3. Устанавливаем gradle   
4. Клонируем репозиторий
5. Теперь можно импортировать gradle-проект в ваш любимый IDE
6. Или можно все делать из консоли. Для этого есть следующие полезные команды:

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

# Редактирование уровней

| tile            | r   | g   | b   | a   | Код        |
|-----------------|-----|-----|-----|-----|------------|
| WallTile        | 0   | 0   | 0   | 255 | 255        |
| FinishTile      | 0   | 255 | 0   | 255 | 16711935   |
| BarrierTile     | 0   | 0   | 255 | 255 | 65535      |
| DeadlyTile      | 255 | 0   | 0   | 255 | -16776961  |
| StartPoint      | 255 | 255 | 0   | 255 | -65281     |
| MovingWallSpace | 0   | 255 | 255 | 255 | 16777215   |
| MovingWallSolid | 255 | 0   | 255 | 255 | -16711681  |
| Empty           | 255 | 255 | 255 | 255 | -          |
| Blades          | 100 | 100 | 100 | 255 | 1684301055 |
