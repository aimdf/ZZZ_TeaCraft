# 🍃 ZZZ_TeaCraft
**Атмосферный чайный плагин для Minecraft серверов**

GitHub:  
https://github.com/aimdf/ZZZ_TeaCraft

---

## 📖 Описание

**ZZZ_TeaCraft** — это атмосферный фермерский плагин для Minecraft, добавляющий **полный цикл выращивания, обработки и употребления чая**.

Плагин фокусируется на:
- постепенном прогрессе,
- визуальной и звуковой обратной связи,
- уникальной механике **напыханости (buzz)**,
- стабильной работе даже на слабых серверах.

От дикого чайного куста до готовой скрутки — весь процесс интегрирован в ванильный геймплей и не нарушает баланс.

---

## 🍃 Основные возможности

### 🌱 Выращивание чая
- Дикие чайные кусты появляются с шансом при ломании травы  
- Полный рост куста занимает около **5 минут**  
- Зрелые кусты визуально выделяются частицами  
- Сбор урожая осуществляется **ножницами**:
  - Выпадает **1–3 чайных плода**
  - **30% шанс** получить саженец  

---

### ☀️ Сушка и обработка
- Чайные плоды сушатся в **рамках предметов**, размещённых на стенах  
- Прогресс сушки отображается **непосредственно в описании предмета**  
- Сушка происходит постепенно, небольшими шагами  
- По завершении процесса предмет превращается в **Сухой чай**

---

### 🚬 Чайные скрутки
- Крафт: **Бумага + Сухой чай → 2 скрутки**
- Использование скрутки повышает уровень **напыханости**
- Напыханость имеет шкалу от **0% до 100%**
- Использование сопровождается визуальными и звуковыми эффектами

---

## 🌀 Механика напыханости (Buzz)

Напыханость — это накапливаемый эффект, который:
- увеличивается при использовании чайных скруток;
- уменьшается со временем или при определённых действиях;
- влияет на поведение игрока и применяемые к нему эффекты.

Механика реализована **плавно и безопасно для производительности**:
- без тиковых нагрузок;
- без массовых проверок;
- с использованием периодических задач.

---

## 💧 Снижение напыханости
- Контакт с водой значительно снижает уровень напыханости
- Присутствует естественный постепенный спад со временем

---

## 🎮 Команды администратора

/teacraft give <player> <item> [amount]
/teacraft bushinfo
/teacraft setstage <grow|mature>


---

## 💾 Хранение данных
- Используется **SQLite** с включённым WAL-режимом
- Автоматическое сохранение чайных кустов
- Восстановление данных после перезапуска сервера
- Очистка невалидных записей

---

## 📥 Установка
1. Скачайте `ZZZ_TeaCraft.jar`
2. Поместите файл в папку `/plugins`
3. Перезапустите сервер
4. Готово — можно начинать чаепитие 🍃

---

## 📊 Основные параметры
- Время роста куста: ~5 минут  
- Время сушки: ~5 минут  
- Плодов с куста: 1–3  
- Шанс саженца: 30%  
- Максимальная напыханость: 100%  
- Скруток с крафта: 2  

---

## 🔧 Требования
- Spigot / Paper **1.16.5+**
- Java **11+**
- SQLite (встроен)

---

## 📝 Лицензия
MIT License — свободное использование и модификация.

---

🍵 **ZZZ_TeaCraft — это не просто предметы, а атмосфера и процесс.**


# 🍃 ZZZ_TeaCraft
**An atmospheric tea-themed plugin for Minecraft servers**

GitHub:  
https://github.com/aimdf/ZZZ_TeaCraft

---

## 📖 Description

**ZZZ_TeaCraft** is an atmospheric farming plugin for Minecraft that adds a **complete tea growing, processing, and consumption cycle**.

The plugin focuses on:
- gradual progression,
- visual and sound feedback,
- a unique **buzz** mechanic,
- stable performance even on low-end servers.

From wild tea bushes to finished tea joints — the entire process is designed to feel natural and balanced within vanilla gameplay.

---

## 🍃 Core Features

### 🌱 Tea Growing
- Wild tea bushes spawn with a chance when breaking grass  
- Full bush growth takes approximately **5 minutes**  
- Mature bushes are visually highlighted with particles  
- Harvesting is done using **shears**:
  - **1–3 tea fruits** per bush  
  - **30% chance** to obtain a sapling  

---

### ☀️ Drying & Processing
- Tea fruits can be dried in **item frames** placed on walls  
- Drying progress is displayed **directly in the item lore**  
- Progress advances gradually over time  
- Once fully dried, the item turns into **Dry Tea**

---

### 🚬 Tea Joints
- Crafting: **Paper + Dry Tea → 2 tea joints**
- Using a joint increases the player’s **buzz level**
- Buzz ranges from **0% to 100%**
- Usage is accompanied by visual and sound effects

---

## 🌀 Buzz Mechanic

Buzz is a gradual, accumulative mechanic that:
- increases when using tea joints;
- decreases over time or through certain actions;
- affects player behavior and applied effects.

The system is designed to be **performance-friendly**:
- no heavy per-tick logic;
- no global player scans;
- based on scheduled tasks rather than constant checks.

---

## 💧 Buzz Reduction
- Contact with water significantly reduces the buzz level  
- Natural decay occurs gradually over time

---

## 🎮 Admin Commands

/teacraft give <player> <item> [amount]
/teacraft bushinfo
/teacraft setstage <grow|mature>

---

## 💾 Data Storage
- Uses **SQLite** with WAL mode enabled
- Automatic saving of tea bushes
- Data restoration on server restart
- Cleanup of invalid records

---

## 📥 Installation
1. Download `ZZZ_TeaCraft.jar`
2. Place the file into the `/plugins` folder
3. Restart the server
4. Enjoy your tea experience 🍃

---

## 📊 Core Parameters
- Bush growth time: ~5 minutes  
- Drying time: ~5 minutes  
- Fruits per bush: 1–3  
- Sapling drop chance: 30%  
- Maximum buzz: 100%  
- Joints per craft: 2  

---

## 🔧 Requirements
- Spigot / Paper **1.16.5+**
- Java **11+**
- SQLite (included)

---

## 📝 License
MIT License — free to use and modify.

---

🍵 **ZZZ_TeaCraft is about atmosphere, process, and immersion — not just items.**
