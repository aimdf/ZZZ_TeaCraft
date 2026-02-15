// ==================== Файл: Constants.java (обновленный) ====================
package com.zzz;

public final class Constants {
    private Constants() {}

    // ==================== ОСНОВНЫЕ КОНСТАНТЫ ====================
    public static final int GROW_TIME = 300;
    public static final int DRY_TIME = 300;
    public static final int BUZZ_INCREMENT = 20;
    public static final int WATER_REDUCTION = 30;
    public static final int NATURAL_DECAY = 1;
    public static final long DECAY_INTERVAL = 1200;

    // ==================== МИНИМАЛЬНЫЙ УРОВЕНЬ ДЛЯ ЭФФЕКТОВ ====================
    public static final int EFFECTS_LEVEL_MIN = 31;

    // ==================== ГЛОБАЛЬНЫЙ КУЛДАУН ЭФФЕКТОВ ====================
    public static final long GLOBAL_COOLDOWN_LOW = 600;
    public static final long GLOBAL_COOLDOWN_MED = 400;
    public static final long GLOBAL_COOLDOWN_HIGH = 200;

    // ==================== НАСТРОЙКИ ЭФФЕКТОВ ====================
    // Темнота
    public static final int DARKNESS_LEVEL_MIN = 61;
    public static final double DARKNESS_CHANCE_MED = 0.15;
    public static final double DARKNESS_CHANCE_HIGH = 0.30;
    public static final int DARKNESS_DURATION_MED = 60;
    public static final int DARKNESS_DURATION_HIGH = 100;

    // Размытость
    public static final int BLUR_LEVEL_MIN = 31;
    public static final double BLUR_CHANCE_LOW = 0.10;
    public static final double BLUR_CHANCE_MED = 0.20;
    public static final double BLUR_CHANCE_HIGH = 0.30;
    public static final int BLUR_DURATION_LOW = 60;
    public static final int BLUR_DURATION_MED = 100;
    public static final int BLUR_DURATION_HIGH = 160;
    public static final int BLUR_AMPLIFIER_LOW = 0;
    public static final int BLUR_AMPLIFIER_MED = 1;
    public static final int BLUR_AMPLIFIER_HIGH = 1;

    // Паранойя - поворот головы
    public static final int HEADTWITCH_LEVEL_MIN = 31;
    public static final double HEADTWITCH_CHANCE_LOW = 0.05;
    public static final double HEADTWITCH_CHANCE_MED = 0.10;
    public static final double HEADTWITCH_CHANCE_HIGH = 0.20;
    public static final int HEADTWITCH_ANGLE_LOW = 90;
    public static final int HEADTWITCH_ANGLE_MED = 135;
    public static final int HEADTWITCH_ANGLE_HIGH = 180;

    // Промах стрельбой
    public static final int MISS_LEVEL_MIN = 31;
    public static final double MISS_CHANCE_LOW = 0.15;
    public static final double MISS_CHANCE_MED = 0.30;
    public static final double MISS_CHANCE_HIGH = 0.50;

    // Искажение сообщений
    public static final int CHATDISTORT_LEVEL_MIN = 31;
    public static final double CHATDISTORT_CHANCE_LOW = 0.40;
    public static final double CHATDISTORT_CHANCE_MED = 0.60;
    public static final double CHATDISTORT_CHANCE_HIGH = 0.80;
    public static final int CHATDISTORT_REPEAT_LOW_MIN = 2;
    public static final int CHATDISTORT_REPEAT_LOW_MAX = 4;
    public static final int CHATDISTORT_REPEAT_MED_MIN = 3;
    public static final int CHATDISTORT_REPEAT_MED_MAX = 5;
    public static final int CHATDISTORT_REPEAT_HIGH_MIN = 4;
    public static final int CHATDISTORT_REPEAT_HIGH_MAX = 7;

    // Искажение ника
    public static final int NAMEDISTORT_LEVEL_MIN = 31;
    public static final double NAMEDISTORT_CHANCE_LOW = 0.30;
    public static final double NAMEDISTORT_CHANCE_MED = 0.50;
    public static final double NAMEDISTORT_CHANCE_HIGH = 0.70;
    public static final int NAMEDISTORT_CHANGES_LOW = 2;
    public static final int NAMEDISTORT_CHANGES_MED = 3;
    public static final int NAMEDISTORT_CHANGES_HIGH = 5;

    // Кошачий язык
    public static final int CATLANG_LEVEL_MIN = 31;
    public static final double CATLANG_CHANCE_LOW = 0.20;
    public static final double CATLANG_CHANCE_MED = 0.40;
    public static final double CATLANG_CHANCE_HIGH = 0.60;

    // Случайные прыжки
    public static final int JUMP_LEVEL_MIN = 31;
    public static final int JUMP_FREQ_LOW_MIN = 3;
    public static final int JUMP_FREQ_LOW_MAX = 6;
    public static final int JUMP_FREQ_MED_MIN = 8;
    public static final int JUMP_FREQ_MED_MAX = 12;
    public static final int JUMP_FREQ_HIGH_MIN = 6;
    public static final int JUMP_FREQ_HIGH_MAX = 10;
    public static final float JUMP_POWER_LOW = 0.45f;
    public static final float JUMP_POWER_MED = 0.48f;
    public static final float JUMP_POWER_HIGH = 0.55f;

    // Дрожание камеры
    public static final int SHAKE_LEVEL_MIN = 31;
    public static final int SHAKE_FREQ_LOW_MIN = 3;
    public static final int SHAKE_FREQ_LOW_MAX = 4;
    public static final int SHAKE_FREQ_MED_MIN = 5;
    public static final int SHAKE_FREQ_MED_MAX = 7;
    public static final int SHAKE_FREQ_HIGH_MIN = 8;
    public static final int SHAKE_FREQ_HIGH_MAX = 12;
    public static final int SHAKE_DURATION_LOW = 40;
    public static final int SHAKE_DURATION_MED = 60;
    public static final int SHAKE_DURATION_HIGH = 80;
    public static final float SHAKE_AMPLITUDE_YAW_LOW = 1.0f;
    public static final float SHAKE_AMPLITUDE_YAW_MED = 2.0f;
    public static final float SHAKE_AMPLITUDE_YAW_HIGH = 3.0f;
    public static final float SHAKE_AMPLITUDE_PITCH_LOW = 0.50f;
    public static final float SHAKE_AMPLITUDE_PITCH_MED = 1.0f;
    public static final float SHAKE_AMPLITUDE_PITCH_HIGH = 1.5f;

    // Искажение скорости
    public static final int SPEEDWARP_LEVEL_MIN = 31;
    public static final int SPEEDWARP_FREQ_LOW_MIN = 2;
    public static final int SPEEDWARP_FREQ_LOW_MAX = 3;
    public static final int SPEEDWARP_FREQ_MED_MIN = 4;
    public static final int SPEEDWARP_FREQ_MED_MAX = 5;
    public static final int SPEEDWARP_FREQ_HIGH_MIN = 6;
    public static final int SPEEDWARP_FREQ_HIGH_MAX = 8;
    public static final int SPEEDWARP_DURATION_LOW = 40;
    public static final int SPEEDWARP_DURATION_MED = 60;
    public static final int SPEEDWARP_DURATION_HIGH = 80;
    public static final int SPEEDWARP_AMPLIFIER_LOW = 0;
    public static final int SPEEDWARP_AMPLIFIER_MED = 1;
    public static final int SPEEDWARP_AMPLIFIER_HIGH = 1;

    // Случайные звуки
    public static final int SOUND_LEVEL_MIN = 31;
    public static final int SOUND_FREQ_LOW_MIN = 1;
    public static final int SOUND_FREQ_LOW_MAX = 2;
    public static final int SOUND_FREQ_MED_MIN = 2;
    public static final int SOUND_FREQ_MED_MAX = 4;
    public static final int SOUND_FREQ_HIGH_MIN = 4;
    public static final int SOUND_FREQ_HIGH_MAX = 6;
    public static final float SOUND_VOLUME_LOW = 0.5f;
    public static final float SOUND_VOLUME_MED = 0.8f;
    public static final float SOUND_VOLUME_HIGH = 1.0f;

    // Фантомные частицы
    public static final int PARTICLE_LEVEL_MIN = 31;
    public static final int PARTICLE_FREQ_LOW = 200;
    public static final int PARTICLE_FREQ_MED = 100;
    public static final int PARTICLE_FREQ_HIGH = 60;
    public static final int PARTICLE_COUNT_LOW = 8;
    public static final int PARTICLE_COUNT_MED = 12;
    public static final int PARTICLE_COUNT_HIGH = 20;

    // Искажение предметов
    public static final int ITEMRENAME_LEVEL_MIN = 31;
    public static final double ITEMRENAME_CHANCE_LOW = 0.30;
    public static final double ITEMRENAME_CHANCE_MED = 0.50;
    public static final double ITEMRENAME_CHANCE_HIGH = 0.70;
    public static final int ITEMRENAME_COUNT_LOW = 2;
    public static final int ITEMRENAME_COUNT_MED = 4;
    public static final int ITEMRENAME_COUNT_HIGH = 7;
}