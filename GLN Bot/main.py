import telebot
import os
from datetime import datetime
from sqlalchemy.exc import IntegrityError, InvalidRequestError
from database.db import Database
from database.models import User

TOKEN = "1949761630:AAEx-kJUCDVstVkd5nQNUPlxF1xrRpKNNjw"
TEST_payment_token = "381764678:TEST:27635 2021-07-19 14:01"
admin_password = "pass"
txt_dt = datetime.now()
txt_dt2 = datetime.now()
bot = telebot.TeleBot(TOKEN)

db = Database('sqlite:///bets.db')

ordinar = 'img/ordinar/photos/file_1.jpeg'
express = 'img/express/photos/file_1.jpeg'


@bot.message_handler(commands=['start'])
def say_hello(message):
    mark = telebot.types.ReplyKeyboardMarkup(True)
    mark.row("Продолжить")
    try:
        db.add(User(message.chat.id, False, False))
        send = bot.send_message(message.chat.id, "Добро пожаловать,\n"
                                                 "Это ваш личный помощник от сообщества G&L News\n"
                                                 "Нажмите продолжить", reply_markup=mark)
        bot.register_next_step_handler(send, menu_user)
    except IntegrityError:
        db.roll_back()
        send = bot.send_message(message.chat.id, "Добро пожаловать,\n"
                                                 "Это ваш личный помощник от сообщества G&L News\n"
                                                 "Нажмите продолжить", reply_markup=mark)
        bot.register_next_step_handler(send, menu_user)


def admin_pass_check(message):
    if message.text == admin_password:
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.row("Продолжить")
        send = bot.send_message(message.chat.id, "Вы успешно авторизированы\n"
                                                 "Нажмите продолжить", reply_markup=mark)
        bot.register_next_step_handler(send, menu_admin)
    else:
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.row("Продолжить")
        send = bot.send_message(message.chat.id, "Неверный пароль\n"
                                                 "Нажмите продолжить", reply_markup=mark)
        bot.register_next_step_handler(send, menu_user)


def menu_admin(message):
    mark = telebot.types.ReplyKeyboardMarkup(True)
    mark.row("Бесплатная аналитика")
    mark.add("Добавить ординар", "Добавить экспресс")
    mark.row("Покинуть панель")
    send = bot.send_message(message.chat.id, "Ниже перечислен допустимый функционал\n", reply_markup=mark)
    bot.register_next_step_handler(send, menu_admin_choosing)


def menu_admin_choosing(message):
    if message.text == "Бесплатная аналитика":
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.row("Вернуться")
        send = bot.send_message(message.chat.id, "Пожалуйста, отправьте фотографию для рассылки\n\n"
                                                 "Рассылка начнется сразу после отправки фото\n"
                                                 "Чтобы вернуться нажмите соответствующую кнопку", reply_markup=mark)
        bot.register_next_step_handler(send, menu_admin_photo_syncing)
    elif message.text == "Добавить ординар":
        send = bot.send_message(message.chat.id, "Введите дату и время через пробел до которого можно приобрести "
                                                 "данный ординар\n\n"
                                                 "В формате:\n"
                                                 "год месяц день часы минуты(опционально)")
        bot.register_next_step_handler(send, menu_admin_add_date)
    elif message.text == "Добавить экспресс":
        send = bot.send_message(message.chat.id, "Введите дату и время через пробел до которого можно приобрести "
                                                 "данный экспресс\n\n"
                                                 "В формате:\n"
                                                 "год месяц день часы минуты(опционально)")
        bot.register_next_step_handler(send, menu_admin_add_date_for_express)
    elif message.text == "Покинуть панель":
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.add("Продолжить")
        send = bot.send_message(message.chat.id, "Нажмите продолжить",
                                reply_markup=mark)
        bot.register_next_step_handler(send, menu_user)
    else:
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.add("Вернуться")
        send = bot.send_message(message.chat.id, "Неизвестный раздел меню\n"
                                                 "Нажмите вернуться",
                                reply_markup=mark)
        bot.register_next_step_handler(send, menu_admin)


def menu_admin_add_date_for_express(message):
    global txt_dt2
    dt = message.text.split(' ')
    try:
        txt_dt2 = datetime(int(dt[0]), int(dt[1]), int(dt[2]), int(dt[3], int(dt[4])))
    except ValueError:
        txt_dt2 = datetime(int(dt[0]), int(dt[1]), int(dt[2]), int(dt[3]))
    mark = telebot.types.ReplyKeyboardMarkup(True)
    mark.row("Вернуться")
    send = bot.send_message(message.chat.id, "Пожалуйста, отправьте фотографию ординара\n"
                                             "Чтобы вернуться нажмите соответствующую кнопку", reply_markup=mark)
    bot.register_next_step_handler(send, menu_admin_add_express_photo)


def menu_admin_add_express_photo(message):
    if message.content_type == 'photo':
        global express
        os.remove(express)
        file_info = bot.get_file(message.photo[len(message.photo) - 1].file_id)
        downloaded_file = bot.download_file(file_info.file_path)
        express = 'img/express/' + file_info.file_path
        with open(express, 'wb') as new_file:
            new_file.write(downloaded_file)
        db.add_express_to_users()
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.row("Продолжить")
        send = bot.send_message(message.chat.id, "экспресс добавлен\n"
                                                 "Нажмите продолжить",
                                reply_markup=mark)
        bot.register_next_step_handler(send, menu_admin)
    else:
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.row("Продолжить")
        send = bot.send_message(message.chat.id, "Процесс добавления остановлен, нажмите продолжить, "
                                                 "чтобы вернуться",
                                reply_markup=mark)
        bot.register_next_step_handler(send, menu_admin)


def menu_admin_add_date(message):
    global txt_dt
    dt = message.text.split(' ')
    try:
        txt_dt = datetime(int(dt[0]), int(dt[1]), int(dt[2]), int(dt[3], int(dt[4])))
    except ValueError:
        txt_dt = datetime(int(dt[0]), int(dt[1]), int(dt[2]), int(dt[3]))

    mark = telebot.types.ReplyKeyboardMarkup(True)
    mark.row("Вернуться")
    send = bot.send_message(message.chat.id, "Пожалуйста, отправьте фотографию ординара\n"
                                             "Чтобы вернуться нажмите соответствующую кнопку", reply_markup=mark)
    bot.register_next_step_handler(send, menu_admin_add_ordinar_photo)


def menu_admin_add_ordinar_photo(message):
    if message.content_type == 'photo':
        global ordinar
        os.remove(ordinar)
        file_info = bot.get_file(message.photo[len(message.photo) - 1].file_id)
        downloaded_file = bot.download_file(file_info.file_path)
        ordinar = 'img/ordinar/' + file_info.file_path
        with open(ordinar, 'wb') as new_file:
            new_file.write(downloaded_file)
        db.add_bills_to_users()
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.row("Продолжить")
        send = bot.send_message(message.chat.id, "Ординар добавлен\n"
                                                 "Нажмите продолжить",
                                reply_markup=mark)
        bot.register_next_step_handler(send, menu_admin)
    else:
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.row("Продолжить")
        send = bot.send_message(message.chat.id, "Процесс добавления остановлен, нажмите продолжить, "
                                                 "чтобы вернуться",
                                reply_markup=mark)
        bot.register_next_step_handler(send, menu_admin)


def menu_admin_photo_syncing(message):
    if message.content_type == 'photo':
        file_info = bot.get_file(message.photo[len(message.photo) - 1].file_id)
        downloaded_file = bot.download_file(file_info.file_path)
        src = 'img/' + file_info.file_path
        with open(src, 'wb') as new_file:
            new_file.write(downloaded_file)
        try:
            users = db.get_users_from_user()
            img = open(src, 'rb')
            for u in range(len(users)):
                bot.send_photo(users[u].chat_number, img)

            mark = telebot.types.ReplyKeyboardMarkup(True)
            mark.row("Продолжить")
            img.close()
            os.remove(src)
            send = bot.reply_to(message, "Рассылка сообщений завершена\n"
                                         "Нажмите продолжить", reply_markup=mark)
            bot.register_next_step_handler(send, menu_admin)
        except InvalidRequestError:
            mark = telebot.types.ReplyKeyboardMarkup(True)
            mark.row("Продолжить")
            img = open(src, 'rb')
            img.close()
            os.remove(src)
            send = bot.reply_to(message, "Произошла ошибка, при отправке сообщений\n"
                                         "Возможно - пользователей в боте не обнаружено", reply_markup=mark)
            bot.register_next_step_handler(send, menu_admin)

    else:
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.row("Продолжить")
        send = bot.send_message(message.chat.id, "Процесс отправки остановлен, нажмите продолжить, чтобы вернуться",
                                reply_markup=mark)
        bot.register_next_step_handler(send, menu_admin)


def menu_user(message):
    if message.text == "admin":
        send = bot.send_message(message.chat.id, "Чтобы продолжить введите пароль администратора")
        bot.register_next_step_handler(send, admin_pass_check)
    else:
        mark = telebot.types.ReplyKeyboardMarkup(resize_keyboard=True)
        mark.add("Ординар ⚽️", "Экспресс 🏀⚽️")
        mark.row("FAQ ⚒")
        mark.add("Список букмейкеров")
        send = bot.send_message(message.chat.id, "Информация о спортивных событиях представлена ниже\n\n"
                                                 "Выберите подходящий раздел меню",
                                reply_markup=mark)
        bot.register_next_step_handler(send, menu_user_choosing)


def menu_user_choosing(message):
    if message.text == "FAQ ⚒":
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.row("Вернуться")
        send = bot.send_message(message.chat.id,
                                "Мы собрали список наиболее актуальных вопросов, которыми могут задаться наши клиенты при знакомстве с компанией⁉️\n\n"
                                "1️⃣Вы занимаетесь раскруткой счётов? Берёте чужие аккаунты для работы? \n\n"
                                "Данной услуги у нас не представлено и никогда не будет\n❗️Мы не работаем с чужими аккаунтами и не принимаем денежные средства для подобных махинаций ❌\n\n"
                                "2️⃣Являетесь ли вы партнёром 1Xbet, 1Win, Melbet ? \n\n"
                                "Компания не сотрудничает ни с одним букмекером. Ссылки на букмекерские конторы вы также не сможете найти. \n"
                                "Мы только можем предложить вам список легальных букмекерских контор на территории Российской Федерации где вы сможете воспользоваться информацией приобретённой у нашей организации.\n\n"
                                "3️⃣Могу ли я получить информацию о спортивных событиях бесплатно? \n\n"
                                "Конечно можете✅ \n"
                                "Прогнозы напрямую публикуется  в нашем Боте, все что вам нужно сделать  - нажать на кнопку🤖\n\n"
                                "4️⃣Вы предоставляете информацию на все виды спорта?\n\n"
                                "Наше сообщество делает акцент на определённых видах спорта - Футбол ⚽️ , Баскетбол 🏀, Теннис 🎾 . Также в аналитике мы используем лиги/турниры только высокого уровня. Для футбола это: Италия (Seria A)🇮🇹, Франция (Ligue 1) 🇫🇷, Германия (Bundesliga)🇩🇪, Испания (LaLiga) 🇪🇸, Англия (Premier League) 🏴󠁧󠁢󠁥󠁮󠁧󠁿. Баскетбол-NBA 🇺🇸. Теннис- ATP-1000\n\n"
                                "5️⃣Платные услуги будут публиковаться каждый день?\n\n"
                                "К сожалению, нет. В зависимости от количества спортивных событий и полноценной аналитики мы будем публиковать информацию в нашем Боте🤖 В некоторые дни Бот может быть пуст, так как мы не нашли подходящих спортивных событий  для предоставления.\n\n"
                                "6️⃣Могут ли пользоваться вашими услугами лица не достигшие 18 лет?\n\n"
                                "Воспользоваться информацией нашего сообщества можно, даже если вы не достигли возраста 18 лет. Однако использовать полученную информацию у легальных букмекеров, к сожалению, не получится, так как каждая контора позволяет воспользоваться их услугами только лицам достигших возраста 18 лет.\n\n"
                                "С Уважением,\n"
                                "Администрация G&L News", reply_markup=mark)
        bot.register_next_step_handler(send, menu_user)
    elif message.text == "Ординар ⚽️":
        status = db.get_status_by_chat_id(message.chat.id)
        status = status.billed
        if (not status) and (datetime.now() <= txt_dt):
            mark = telebot.types.ReplyKeyboardMarkup(True)
            mark.row("Вернуться")
            bot.send_message(message.chat.id, "Данная услуга предоставляет Вам информацию на ординарное спортивное "
                                              "событие с коэффициентом \n"
                                              "от 1.55 до 1.85\n\n"
                                              "Для приобретения данной информации нажмите кнопку оплатить\n"
                                              "Для возврата в главное меню нажмите вернуться", reply_markup=mark)
            bot.send_invoice(
                chat_id=message.chat.id,
                title='Приобретение информации',
                description="Данная услуга предоставляет Вам информацию на ординарное спортивное "
                            "событие с коэффициентом\n"
                            "от 1.55 до 1.85\n",
                invoice_payload='true',
                provider_token="381764678:TEST:27635",
                start_parameter='true',
                currency='RUB',
                prices=[telebot.types.LabeledPrice(label='Информация', amount=45000)]
            )
        else:
            mark = telebot.types.ReplyKeyboardMarkup(True)
            mark.add("Вернуться")
            send = bot.send_message(message.chat.id, "Новых ординаров пока не появлялось\n"
                                                     "Попробуйте вернуться сюда позже", reply_markup=mark)
            bot.register_next_step_handler(send, menu_user)
    elif message.text == "Экспресс 🏀⚽️":
        status = db.get_status_by_chat_id(message.chat.id)
        status = status.express
        if (not status) and (datetime.now() <= txt_dt2):
            mark = telebot.types.ReplyKeyboardMarkup(True)
            mark.row("Вернуться")
            bot.send_message(message.chat.id, "Данная услуга предоставляет Вам информацию на экспресс с коэффициентом\n"
                                              "от 2.2\n\n"
                                              "Для приобретения данной информации нажмите кнопку оплатить\n"
                                              "Для возврата в главное меню нажмите вернуться", reply_markup=mark)
            bot.send_invoice(
                chat_id=message.chat.id,
                title='Приобретение информации',
                description="Данная услуга предоставляет Вам информацию на экспресс с коэффициентом\n"
                            "от 2.2\n",
                invoice_payload='true',
                provider_token="381764678:TEST:27635",
                start_parameter='true',
                currency='RUB',
                prices=[telebot.types.LabeledPrice(label='Информация', amount=90000)]
            )
        else:
            mark = telebot.types.ReplyKeyboardMarkup(True)
            mark.add("Вернуться")
            send = bot.send_message(message.chat.id, "Новых экспрессов пока не появлялось\n"
                                                     "Попробуйте вернуться сюда позже", reply_markup=mark)
            bot.register_next_step_handler(send, menu_user)
    elif message.text == "admin":
        send = bot.send_message(message.chat.id, "Чтобы продолжить введите пароль администратора")
        bot.register_next_step_handler(send, admin_pass_check)
    elif message.text == "Список букмейкеров":
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.add("Вернуться")
        send = bot.send_message(message.chat.id,
                                "Данные букмекерские конторы являются легальными в Российской Федерации\n"
                                "В этих конторах вы можете использовать информацию приобретенную у нашей компании\n"
                                "1️⃣ Winline\n"
                                "2️⃣ FonBet\n"
                                "3️⃣ OlimpBet\n"
                                "4️⃣ Pari Match\n"
                                "5️⃣ Liga Stavok\n"
                                "Для возвращения в меню нажмите вернуться",
                                reply_markup=mark)
        bot.register_next_step_handler(send, menu_user)
    else:
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.add("Вернуться")
        send = bot.send_message(message.chat.id, "Неизвестный раздел меню\n"
                                                 "Нажмите вернуться",
                                reply_markup=mark)
        bot.register_next_step_handler(send, menu_user)


@bot.pre_checkout_query_handler(func=lambda query: True)
def process_pre_checkout_query(pre_checkout_query: telebot.types.PreCheckoutQuery):
    bot.answer_pre_checkout_query(int(pre_checkout_query.id), ok=True, error_message="Ошибка при оформлении заказа")


@bot.message_handler(content_types=['successful_payment'])
def send_info_after_payment(message):
    if message.successful_payment.total_amount == 45000:
        db.add_bill_by_chat_id(message)
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.add("Продолжить")
        send = bot.send_photo(message.chat.id, open(ordinar, 'rb'), reply_markup=mark)
        bot.register_next_step_handler(send, menu_user)
    else:
        db.add_express_by_chat_id(message)
        mark = telebot.types.ReplyKeyboardMarkup(True)
        mark.add("Продолжить")
        send = bot.send_photo(message.chat.id, open(express, 'rb'), reply_markup=mark)
        bot.register_next_step_handler(send, menu_user)


@bot.message_handler(content_types=['text'])
def menu_user_back_to_menu(message):
    mark = telebot.types.ReplyKeyboardMarkup(True)
    mark.add("Продолжить")
    send = bot.send_message(message.chat.id, "Операция отменена\n"
                                             "Нажмите продолжить", reply_markup=mark)
    bot.register_next_step_handler(send, menu_user)


if __name__ == '__main__':
    bot.polling(none_stop=True)
