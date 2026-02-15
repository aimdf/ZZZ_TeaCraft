# 🍃 ZZZ_TeaCraft

**An atmospheric tea-brewing plugin for Minecraft servers**

**GitHub:** https://github.com/aimdf/ZZZ_TeaCraft

---

## 📖 Description

Turn your server into a cozy tea house. **ZZZ_TeaCraft** adds a complete farming cycle: from finding a wild bush in the tall grass to crafting and using a tea joint. Experience a unique "Buzz" system that alters your perception with visual and auditory hallucinations, camera shakes, and chat distortions. Ready for some friendly hallucinations and a sudden urge to meow?

The plugin focuses on:
- **Meaningful Progression:** Bushes need care and time to grow.
- **Deep "Buzz" Mechanic:** Over 15 different effects that scale with your buzz level, creating a unique atmosphere.
- **Visual & Audio Feedback:** Particles, sounds, and chat changes make the world feel different.
- **Stability & Performance:** Optimized code for servers of all sizes.

---

## ✨ Key Features

### 🌱 Complete Growth Cycle
- **Realistic Growth:** Bushes require care and watering.
- **Moisture System:** Affects growth speed (10% to 100% speed).
- **Visual Cues:** Mature bushes glow with particles.
- **Harvesting:** Use shears for a chance to get a new sapling.

### ☀️ Drying System
- **Innovative Method:** Dry your tea in item frames.
- **Visual Progress:** Drying percentage is displayed in the item's lore.
- **Gradual Transformation:** Tea Fruit → Dry Tea.

### 🚬 The Buzz Mechanic
- **0-100% Scale:** Accumulates and naturally decays.
- **Multiple Effects:** Over 15 different sensory and motor effects.
- **Tiered Intensity:** Effects become stronger and weirder as your buzz level increases.

### 🎮 Admin Commands
- Full control over the plugin's mechanics.
- Easy bush and item management.

---

## 🍃 The Complete Production Cycle

### 🌱 From Wild Bush to Plantation

1.  **Finding a Sapling**
    - Breaking tall grass (`SHORT_GRASS`, `TALL_GRASS`, `FERN`, `LARGE_FERN`) has a **5% chance** to drop a Tea Sapling.
    - A successful find is celebrated with happy villager particles.

2.  **Planting**
    - The sapling (`Tea Bush`) is planted on the ground like a normal fern.
    - A database entry is automatically created upon planting.
![1WB](https://cdn.modrinth.com/data/cached_images/fcc2a049021632c8c26d1542fe3876339ed1ae72.webp)

3.  **Care and Watering**
    - Bushes need regular watering with a water bottle.
    - **Right-click** the bush with a **water bottle**: restores **+30% moisture**.
    - **Right-click** the bush with a **glass pane**: displays the bush's current stats (Growth progress, Moisture level) in chat. A handy tool for any aspiring tea farmer!
    - Moisture affects growth speed and is displayed in the bush's info.
    - Without watering, the bush loses **1% moisture every minute**.

4.  **Growth and Maturity**
    - A full growth cycle at optimal moisture (100%) takes **4 hours**.
    - Growth speed scales directly with current moisture (minimum 10% speed at 0% moisture).
    - A mature bush is marked by glowing `END_ROD` particles.

5.  **Harvesting**
    - A mature bush is harvested with **shears** (right-click).
    - Harvest results:
        - **1-3 Tea Fruits** (always)
        - **30% chance** for a new Tea Sapling
    - After harvesting, the bush resets to the growing stage with 100% moisture.

### ☀️ Processing (Drying)

1.  **Placement**
    - Place Tea Fruits into **item frames** mounted on walls.
    - Only item frames already containing a Tea Fruit are valid.
2WB![2WB](https://cdn.modrinth.com/data/cached_images/915c3e315c4d6b320ce1ca7ade43fa260ee9b039.webp)

2.  **Drying Process**
    - The plugin checks item frames every **5 seconds** (100 ticks).
    - Each check increases the fruit's dryness by **+2%**.
    - A full drying cycle takes **~4-5 minutes**.

3.  **Progress Visualization**
    - Drying progress is shown directly in the item's lore.
    - Example: `Drying: ▮▮▮▮▮▯▯▯▯▯ 50%`
    - The green bar visually represents the progress.

4.  **Completion**
    - At 100% dryness, the fruit automatically turns into **Dry Tea**.
    - The item in the frame is replaced without player interaction.

### 🚬 Crafting and Consumption

1.  **Crafting (Shapeless)**
    - Dry Tea + Paper = Tea Joint.
    - Supports shift-clicking for mass crafting.

2.  **Usage**
    - Right-click while holding a Tea Joint (represented as a firework star).
    - Using it is accompanied by clouds of smoke (`CAMPFIRE_COSY_SMOKE`).
    - One joint increases the buzz level by **+20%** (max 100%).
![3WB](https://cdn.modrinth.com/data/cached_images/6481d5dc45eb84c06b5a62e1b6cc71dc17adf005.webp)

3.  **Feedback**
    - Your current buzz level is displayed in chat as a graphical bar.
    - The bar updates after each use.

---

## 🌀 The Buzz Mechanic

The Buzz is the core mechanic. Your level (0-100) accumulates when using joints, decays over time, and dramatically changes your game perception.

### 📊 Intensity Levels

All effects are tiered based on your buzz level:

| Level | Range | Characteristic |
|:-----:|:-----:|----------------|
| **Low** | 31-60 | Mild distortions, friendly hallucinations |
| **Medium** | 61-80 | Noticeable perception and motor impairment |
| **High** | 81-100 | Intense hallucinations, loss of control |

### ⏱️ Global Cooldown System
- Effects don't spam; they appear with a smart delay.
- The cooldown decreases as your buzz level increases (from 30s to 10s), ensuring a comfortable but intense experience.

### 📈 Effects by Category

*(A full detailed table of effects is available in the Russian version or the source code. Key effects include:)*

- **Basic:** Nausea, Slowness, Night Vision.
- **Sensory:** Auditory hallucinations (friendly to scary), temporary Darkness, Blur, various particles.
- **Motor:** Camera shakes, spontaneous jumps, speed warps, chance to miss bow shots.
- **Cognitive:** Chat distortion, "Cat Language" (adding "meow"), name distortion, item renaming.
- **Critical (90+):** Chance to drop the item you're holding.

### 💧 Reducing the Buzz

| Method | Effect | Description |
|:-------|:------:|-------------|
| **Time** | -1% per decay tick | Constant, slow decrease (every 60 sec) |
| **Water** | -30% per bottle | Drinking a regular water bottle |

---

## 🎮 Admin Commands

All commands require the `teacraft.admin` permission.

### Main Commands
- `/teacraft give <player> <item> [amount]`
    Gives the specified item to a player.
    - **bush** — Tea Sapling
    - **fruit** — Tea Fruit
    - **dry** — Dry Tea
    - **joint** — Tea Joint
- `/teacraft bushinfo`
    Shows detailed information about the bush you are looking at (Stage, Growth progress, Moisture). *Note: Regular players can get this info by right-clicking the bush with a glass pane.*
- `/teacraft setstage <growing/mature>`
    Forcefully switches the growth stage of the targeted bush.

### Tab Completion
- `/teacraft ` → `give`, `bushinfo`, `setstage`
- `/teacraft give ` → List of online players
- `/teacraft give <player> ` → `bush`, `fruit`, `dry`, `joint`
- `/teacraft setstage ` → `growing`, `mature`

---

## 💾 Data Storage & Optimization

### 🗄️ Database (SQLite)
- **File:** `plugins/ZZZ_TeaCraft/teabushes.db`
- **Mode:** WAL (Write-Ahead Logging) for performance
- **Tables:**
    - `tea_bushes` — all planted bushes
    - Indexes for fast lookups by coordinates and stage

### 📊 Table Structure
```sql
CREATE TABLE tea_bushes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    world VARCHAR(64) NOT NULL,
    x INTEGER NOT NULL,
    y INTEGER NOT NULL,
    z INTEGER NOT NULL,
    plant_time BIGINT NOT NULL,
    is_mature BOOLEAN NOT NULL DEFAULT 0,
    moisture INTEGER NOT NULL DEFAULT 100,
    last_moisture_update BIGINT NOT NULL,
    planted_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(world, x, y, z)
);
```
## ⚙️ Optimization System

- **Asynchronous Operations:** Database saves are async, using batch requests.

- **Optimized Tasks:**
    - `BushGrowthTask` (1 sec)
    - `MoistureDrainTask` (1 min)
    - `ParticleTask` (0.5 sec, only near players)
    - `CombinedEffectsTask` (0.15 sec)
    - `BuzzDecayTask` (60 sec)
    - `ItemFrameCheckTask` (5 sec)
    - `CleanupTask` (5 min) - Removes invalid bush records.

- **Auto-Cleanup:** Removes DB entries for bushes in unloaded/nonexistent worlds or where the block was broken.

---

## 📥 Installation

1.  **Download** the latest `ZZZ_TeaCraft.jar` from the Releases section.
2.  **Place** the file into your server's `/plugins` folder.
3.  **Restart** your server (or use a plugin manager like PlugMan).
4.  **Check the logs** for a successful startup message.
5.  **Ready to play!**

---

## ⚙️ Configuration Parameters (Constants.java)

⚠️ **Note:** In the current version, all settings are in the `Constants.java` file. A proper `config.yml` is planned for future updates.

| Parameter | Value | Description |
|:----------|:-----:|-------------|
| `GROW_TIME` | 14400 sec | Growth time at 100% moisture (**4 hours**) |
| `WATER_BOTTLE_AMOUNT` | +30% | Moisture restored by a water bottle |
| `MOISTURE_DRAIN_RATE` | -1% | Moisture loss per minute |
| `DRY_TIME` | ~250 sec | Drying time |
| `BUZZ_INCREMENT` | +20% | Buzz increase per joint |
| `NATURAL_DECAY` | -1% | Natural buzz decay per interval (60 sec) |

---

## 🔧 Requirements

- **Server:** Spigot / Paper **1.16.5** or higher
- **Java:** **11** or higher
- **Database:** SQLite (built-in)

---

## 📝 License

**MIT License** © 2024 aimdf

---

## 🍵 About the Project

**ZZZ_TeaCraft** is more than just a set of items; it's a whole relaxation philosophy for your server. Plant a bush, care for it, harvest it, dry the leaves, roll it, and... relax. The atmosphere is guaranteed.


---

### 📄 README_RU.md (Русский)

# 🍃 ZZZ_TeaCraft

**Атмосферный чайный плагин для Minecraft серверов**

**GitHub:** https://github.com/aimdf/ZZZ_TeaCraft

---

## 📖 Описание

Превратите свой сервер в уютную чайную. **ZZZ_TeaCraft** добавляет полный цикл: от поиска дикого куста в высокой траве до создания и использования чайной скрутки. Испытайте уникальную механику "напыханости" (Buzz), которая меняет ваше восприятие с помощью визуальных и слуховых галлюцинаций, дрожания камеры и искажений в чате. Готовы к дружелюбным галлюцинациям и внезапному желанию помяукать?

Плагин делает акцент на:
- **Постепенном и осмысленном прогрессе:** За кустами нужно ухаживать.
- **Глубокой механике "напыханости" (Buzz):** Более 15 эффектов, усиливающихся с уровнем.
- **Визуальной и звуковой обратной связи:** Частицы, звуки, изменения в чате.
- **Стабильности и производительности:** Оптимизированный код.

---

## ✨ Ключевые особенности

### 🌱 Полный цикл выращивания
- **Реалистичный рост:** Кусты требуют ухода и полива.
- **Система влажности:** Влияет на скорость роста (от 10% до 100%).
- **Визуальные подсказки:** Зрелые кусты светятся частицами.
- **Сбор урожая:** Ножницами с шансом получить саженец.

### ☀️ Система сушки
- **Инновационный метод:** Сушка в рамках для предметов.
- **Визуальный прогресс:** Процент сушки отображается в описании.
- **Постепенное преобразование:** Плод → Сухой чай.

### 🚬 Механика напыханости (Buzz)
- **Шкала 0-100%:** Накопление и естественный спад.
- **Множество эффектов:** Более 15 различных эффектов.
- **Градация по уровням:** Эффекты усиливаются с ростом напыханости.

### 🎮 Команды администратора
- Полный контроль над игровыми процессами.
- Удобное управление кустами и предметами.

---

## 🍃 Полный цикл производства

### 🌱 От дикого куста до плантации

1.  **Поиск саженца**
    - Срубая высокую траву (`SHORT_GRASS`, `TALL_GRASS`, `FERN`, `LARGE_FERN`), вы с **5% шансом** можете найти саженец чайного куста.
    - Удачная находка сопровождается частицами счастливого жителя.

2.  **Посадка**
    - Саженец (`Куст чая`) высаживается на землю как обычный папоротник.
    - При посадке автоматически создается запись в базе данных.
![1WB](https://cdn.modrinth.com/data/cached_images/fcc2a049021632c8c26d1542fe3876339ed1ae72.webp)

3.  **Уход и полив**
    - Кусту требуется регулярный полив водой из бутылки.
    - **ПКМ с бутылкой воды** по кусту: восполняет **+30% влажности**.
    - **ПКМ со стеклянной панелью** по кусту: показывает в чате текущие параметры куста (прогресс роста, влажность). Незаменимый инструмент для каждого чаевода!
    - Влажность влияет на скорость роста и отображается в информации о кусте.
    - Без полива куст теряет **1% влажности каждую минуту**.

4.  **Рост и созревание**
    - Полный цикл роста с оптимальной влажностью (100%) занимает **4 часа**.
    - Скорость роста прямо пропорциональна текущей влажности (минимум 10% при 0% влажности).
    - Зрелый куст помечается светящимися частицами (`END_ROD`).

5.  **Сбор урожая**
    - Зрелый куст срезается **ножницами** (ПКМ).
    - Результат сбора:
        - **1-3 чайных плода** (всегда)
        - **30% шанс** получить новый саженец куста
    - После сбора куст переходит в стадию роста, влажность сбрасывается до 100%.

### ☀️ Обработка сырья (Сушка)

1.  **Размещение**
    - Чайные плоды помещаются в **рамки для предметов**, размещенные на стенах.
    - Подходят только рамки, уже содержащие плод чая.
![2WB](https://cdn.modrinth.com/data/cached_images/915c3e315c4d6b320ce1ca7ade43fa260ee9b039.webp)

2.  **Процесс сушки**
    - Плагин автоматически проверяет рамки каждые **5 секунд** (100 тиков).
    - За каждую проверку плод получает **+2% к сухости**.
    - Полный цикл сушки занимает **~4-5 минут**.

3.  **Визуализация прогресса**
    - Прогресс сушки отображается прямо в описании предмета.
    - Пример: `Сушка: ▮▮▮▮▮▯▯▯▯▯ 50%`
    - Зеленая полоска визуально показывает степень готовности.

4.  **Завершение**
    - При достижении 100% сухости плод автоматически превращается в **Сухой чай**.
    - Предмет в рамке заменяется без участия игрока.

### 🚬 Изготовление и употребление

1.  **Крафт (Бесформенный)**
    - Сухой чай + Бумага = Чайная скрутка.
    - Поддерживается Shift-клик для массового создания.

2.  **Использование**
    - Нажмите ПКМ с чайной скруткой в руке (представлена как звездочка фейерверка).
    - Использование сопровождается клубами дыма (`CAMPFIRE_COSY_SMOKE`).
    - Одна скрутка повышает уровень напыханости на **+20%** (максимум 100%).
![3WB](https://cdn.modrinth.com/data/cached_images/6481d5dc45eb84c06b5a62e1b6cc71dc17adf005.webp)

3.  **Обратная связь**
    - В чате отображается текущий уровень напыханости в виде графической шкалы.
    - Шкала обновляется после каждого использования.

---

## 🌀 Механика напыханости (The Buzz)

Напыханость — это ключевая механика плагина. Уровень (от 0 до 100) накапливается при использовании скруток, со временем спадает и кардинально меняет восприятие игры.

### 📊 Уровни интенсивности

| Уровень | Диапазон | Характеристика |
|:-------:|:--------:|----------------|
| **Низкий** | 31-60 | Легкие искажения, дружелюбные галлюцинации |
| **Средний** | 61-80 | Заметные нарушения восприятия и моторики |
| **Высокий** | 81-100 | Сильные галлюцинации, потеря контроля |

### ⏱️ Глобальная система задержек
- Эффекты не спамят, а появляются с умной задержкой.
- Задержка уменьшается с ростом уровня (от 30 до 10 секунд).

### 📈 Эффекты по категориям

*(Полная таблица эффектов доступна в исходном коде. Ключевые эффекты:)*

- **Базовые:** Тошнота, замедление, ночное зрение.
- **Сенсорные:** Слуховые галлюцинации (от дружелюбных до страшных), временная темнота, размытость, различные частицы.
- **Моторные:** Дрожание камеры, спонтанные прыжки, искажение скорости, промах из лука.
- **Когнитивные:** Искажение чата, "кошачий язык" (добавление "мяу"), искажение ника, переименование предметов.
- **Критические (90+):** Шанс выронить предмет из руки.

### 💧 Снижение напыханости

| Способ | Эффект | Описание |
|:-------|:------:|----------|
| **Со временем** | -1% за тик спада | Постоянное, медленное снижение (раз в 60 сек) |
| **Вода** | -30% за бутылку | Выпивание обычной бутылки с водой |

---

## 🎮 Команды администратора

Все команды требуют права `teacraft.admin`.

### Основные команды
- `/teacraft give <игрок> <предмет> [количество]`
    Выдает указанный предмет игроку.
    - **bush** / **куст** — саженец чайного куста
    - **fruit** / **плод** — чайный плод
    - **dry** / **сухой** — сухой чай
    - **joint** / **скрутка** — чайная скрутка
- `/teacraft bushinfo`
    Показывает подробную информацию о кусте, на который смотрит игрок (Стадия, прогресс роста, влажность). *Примечание: обычные игроки могут получить эту информацию, кликнув по кусту стеклянной панелью.*
- `/teacraft setstage <рост/зрелый>`
    Принудительно переключает стадию роста целевого куста.

### Автодополнение
- `/teacraft ` → `give`, `bushinfo`, `setstage`
- `/teacraft give ` → список онлайн-игроков
- `/teacraft give <игрок> ` → `bush`, `fruit`, `dry`, `joint`
- `/teacraft setstage ` → `рост`, `зрелый`

---

## 💾 Хранение данных и оптимизация

### 🗄️ База данных (SQLite)
- **Файл:** `plugins/ZZZ_TeaCraft/teabushes.db`
- **Режим:** WAL для производительности
- **Таблицы:**
    - `tea_bushes` — все посаженные кусты
    - Индексы для быстрого поиска

### 📊 Структура таблицы
```sql
CREATE TABLE tea_bushes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    world VARCHAR(64) NOT NULL,
    x INTEGER NOT NULL,
    y INTEGER NOT NULL,
    z INTEGER NOT NULL,
    plant_time BIGINT NOT NULL,
    is_mature BOOLEAN NOT NULL DEFAULT 0,
    moisture INTEGER NOT NULL DEFAULT 100,
    last_moisture_update BIGINT NOT NULL,
    planted_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(world, x, y, z)
);
```
## ⚙️ Система оптимизации

- **Асинхронные операции:** Сохранение кустов выполняется асинхронно, массовые сохранения используют batch-запросы.

- **Оптимизированные таски:**
    - `BushGrowthTask` (1 сек) — проверка роста кустов
    - `MoistureDrainTask` (1 мин) — потеря влажности
    - `ParticleTask` (0.5 сек) — частицы для зрелых кустов (только рядом с игроками)
    - `CombinedEffectsTask` (0.15 сек) — все эффекты напыханости
    - `BuzzDecayTask` (60 сек) — спад напыханости
    - `ItemFrameCheckTask` (5 сек) — сушка в рамках
    - `CleanupTask` (5 мин) — очистка невалидных записей

- **Автоочистка:** Автоматическое удаление из БД записей о кустах в выгруженных или несуществующих мирах, а также там, где блок был сломан.

---

## 📥 Установка

1.  **Скачайте** последнюю версию `ZZZ_TeaCraft.jar` из релизов.
2.  **Поместите** файл в папку `/plugins` вашего сервера.
3.  **Перезапустите** сервер (или используйте плагин для менеджмента плагинов).
4.  **Проверьте логи** — там должно быть сообщение об успешном запуске.
5.  **Готово!** Можно начинать играть.

---

## ⚙️ Параметры конфигурации (Constants.java)

⚠️ **Внимание:** В текущей версии все настройки задаются в файле `Constants.java`. Мы работаем над вынесением их в удобный `config.yml` в будущих обновлениях.

| Параметр | Значение | Описание |
|:---------|:--------:|----------|
| `GROW_TIME` | 14400 сек | Время роста при 100% влажности (**4 часа**) |
| `WATER_BOTTLE_AMOUNT` | +30% | Восполнение влажности бутылкой воды |
| `MOISTURE_DRAIN_RATE` | -1% | Потеря влажности в минуту |
| `DRY_TIME` | ~250 сек | Время сушки |
| `BUZZ_INCREMENT` | +20% | Прирост напыханости за одну скрутку |
| `NATURAL_DECAY` | -1% | Естественный спад напыханости за интервал (60 сек) |

---

## 🔧 Требования

- **Сервер:** Spigot / Paper **1.16.5** или выше
- **Java:** **11** или выше
- **База данных:** SQLite (встроена)

---

## 📝 Лицензия

**MIT License** © 2024 aimdf

---

## 🍵 О проекте

**ZZZ_TeaCraft** — это не просто набор предметов, а целая философия отдыха на вашем сервере. Посадите куст, ухаживайте за ним, соберите урожай, высушите листья, скрутите и... расслабьтесь. Атмосфера гарантирована.
