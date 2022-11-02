package steelUnicorn.laplacity.utils;

import steelUnicorn.laplacity.core.Laplacity;

/**
 * Интерфейс для обработки уведомлений. Нужен для хранения параметров уведомлений, и их создания.
 */
public interface NotificationHandler {
    int RESTORE_LOWER_BOUND = Laplacity.isDebugEnabled() ? 10 * 60 : 30 * 60; //10 min in debug and 3 food in release
    /**
     * Устанавливает AlarmManager для уведомления о восстановлении еды у кота.
     */
    void setFoodAlarm();

    /**
     * Устанавливает время для восстановления еды у кота.
     * @param time время в секундах
     */
    void setRestoreTime(int time);
}
