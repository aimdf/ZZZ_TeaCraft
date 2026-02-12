# 🍃 ZZZ_TeaCraft
**«Чайная» магия для вашего сервера!**

GitHub:  
[Git](https://github.com/aimdf/ZZZ_TeaCraft)

## 📖 Description

ZZZ_TeaCraft — это атмосферный фермерский плагин для Minecraft серверов, добавляющий полный цикл выращивания, обработки и употребления «чая».
От дикого куста до ароматной скрутки — погрузитесь в «чайную» культуру с уникальной механикой напыханости.


## 🍃 Key Features

### 🌱 Выращивание «чая»
- Дикие кусты «чая» с 5% шансом при ломании травы
- 5 минут роста до зрелости
- Визуальные частицы у зрелых кустов (END_ROD + HAPPY_VILLAGER)
- Сбор урожая ножницами:
  - 1–3 плода
  - 30% шанс выпадения саженца


![Teacust](https://cdn.modrinth.com/data/cached_images/fcc2a049021632c8c26d1542fe3876339ed1ae72.webp)


### ☀️ Сушка и обработка
- Сушка «чайных» плодов в рамках на стене
- Прогресс сушки отображается в описании предмета
- 2% каждые 5 секунд (полный цикл около 4 минут)
- При 100% превращается в «Сухой чай»

![asa](https://cdn.modrinth.com/data/cached_images/915c3e315c4d6b320ce1ca7ade43fa260ee9b039.webp)

### 🚬 «Чайные» скрутки
- Крафт: Бумага + Сухой «чай» = 2 скрутки
- Уникальный эффект [Напыханость]
- Шкала от 0% до 100% с визуальным баром
- Частицы дыма при использовании

![joint](https://cdn.modrinth.com/data/cached_images/6481d5dc45eb84c06b5a62e1b6cc71dc17adf005.webp)

## 🌀 Эффекты напыханости

Зависят от текущего уровня:
- Более 10% — шанс тошноты (5 секунд)
- 25% и выше — замедление I–IV
- 50% и выше — ночное зрение
- 70% и выше — шанс слепоты
- 90% и выше — шанс выронить предмет

## 💧 Снижение эффекта
- Вода снижает напыханость на 30%
- Естественный спад — 1% в минуту

## 🎮 Команды администратора
/teacraft give <player> <item> [amount]  
/teacraft bushinfo  
/teacraft setstage <grow|mature>

## 💾 База данных
- SQLite с WAL режимом
- Автосохранение и восстановление «чайных» кустов
- Очистка невалидных данных

## 📥 Установка
1. Скачайте ZZZ_TeaCraft.jar
2. Поместите файл в папку /plugins
3. Перезапустите сервер
4. Наслаждайтесь «чаем»

## 🎯 Почему ZZZ_TeaCraft?
- Полный цикл — от семечка до эффекта
- Атмосфера — частицы, звуки, визуальный прогресс
- Балансированный геймплей
- Оптимизация и надёжное сохранение данных
- Открытый исходный код

## 📊 Статистика
- Рост куста: 5 минут
- Сушка: 5 минут
- Плодов с куста: 1–3
- Шанс саженца: 30%
- Максимальная напыханость: 100%
- Скруток с крафта: 2

## 🔧 Требования
- Spigot / Paper 1.16.5+
- Java 11+
- SQLite (встроен)

## 📝 Лицензия
MIT License

Приятного «чаепития»!

_______________________________


# 🍃 ZZZ_TeaCraft
**“Tea” magic for your server!**

GitHub:  
[Git](https://github.com/aimdf/ZZZ_TeaCraft)

## 📖 Description

**ZZZ_TeaCraft** is an atmospheric farming plugin for Minecraft servers that adds a full cycle of growing, processing, and consuming “tea”.

From wild bushes to aromatic joints — immerse yourself in “tea” culture with a unique **[buzz]** mechanic, visual feedback, and balanced gameplay.

## 🍃 Key Features

### 🌱 Tea Growing
- Wild “tea” bushes with a 5% chance from breaking grass  
- 5 minutes to fully mature  
- Visual particles on mature bushes (END_ROD + HAPPY_VILLAGER)  
- Harvest with shears:
  - 1–3 fruits  
  - 30% sapling drop chance  

![Tea Bush](https://cdn.modrinth.com/data/cached_images/fcc2a049021632c8c26d1542fe3876339ed1ae72.webp)

### ☀️ Drying & Processing
- Dry “tea” fruits in item frames placed on walls  
- Drying progress shown directly in item lore  
- 2% progress every 5 seconds (about 4 minutes total)  
- Transforms into “Dry Tea” at 100%  

![Drying](https://cdn.modrinth.com/data/cached_images/915c3e315c4d6b320ce1ca7ade43fa260ee9b039.webp)

### 🚬 Tea Joints
- Crafting: Paper + Dry “Tea” = 2 joints  
- Unique **[Buzz]** effect  
- 0–100% buzz scale with visual bar  
- Smoke particles on use  

![Joint](https://cdn.modrinth.com/data/cached_images/6481d5dc45eb84c06b5a62e1b6cc71dc17adf005.webp)

## 🌀 Buzz Effects
Effects depend on the current buzz level:
- Above 10% — chance of nausea (5 seconds)  
- 25%+ — Slowness I–IV  
- 50%+ — Night Vision  
- 70%+ — Chance of Blindness  
- 90%+ — Chance to drop the held item  

## 💧 Buzz Reduction
- Water reduces buzz by 30%  
- Natural decay — 1% per minute  

## 🎮 Admin Commands
/teacraft give <player> <item> [amount]  
/teacraft bushinfo  
/teacraft setstage <grow|mature>

## 💾 Database
- SQLite with WAL mode enabled  
- Automatic saving and restoration of “tea” bushes  
- Cleanup of invalid data  

## 📥 Installation
1. Download ZZZ_TeaCraft.jar  
2. Place the file into the /plugins folder  
3. Restart the server  
4. Enjoy your “tea” 🍵  

## 📊 Statistics
- Bush growth time: 5 minutes  
- Drying time: 5 minutes  
- Fruits per bush: 1–3  
- Sapling drop chance: 30%  
- Maximum buzz: 100%  
- Joints per craft: 2  

## 🔧 Requirements
- Spigot / Paper 1.16.5+  
- Java 11+  
- SQLite (included)  

## 📝 License
MIT License — free to use and modify.

Enjoy your “tea”! 🍃
