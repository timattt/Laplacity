package steelUnicorn.laplacity.utils;

/**
 * Интерфейс для обработки уведомлений. Нужен для хранения параметров уведомлений, и их создания.
 */
public interface NotificationHandler {
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
