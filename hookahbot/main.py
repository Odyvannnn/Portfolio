import telebot

TOKEN = "1792959858:AAGaxstHBlx_NzpZEL8PP5f5U37tBohSpV8"
bot = telebot.TeleBot(TOKEN)

sorts = ["Обычный", "Грейпфрут", "Апельсин"]
tobacco_sorts = ['1', '2', '3', '4', '5', '6']
tastes = ['Вкус 1', 'Вкус 2', 'Вкус 3', 'Вкус 4', 'Вкус 5', 'Вкус 6']
admins_lib = []


def admin(message):
    global admins_lib
    new_us = telebot.types.ReplyKeyboardMarkup(True)
    new_us.row('Взять заказ')
    send = bot.send_message(message.chat.id, "Жду заказы, я уведомлю вас как только появятся новые заказы\n"
                                             "Нажмите 'взять заказ', как только он появится", reply_markup=new_us)
    admins_lib.append(message.chat.id)
    bot.register_next_step_handler(send, save_order)


def save_order(message):
    new_us = telebot.types.ReplyKeyboardMarkup(True)
    new_us.row('Продолжить')
    send = bot.send_message(message.chat.id, "Заказ закреплен за вами, не забудьте связаться с клиентом\n"
                                             "Нажмите прододжить, чтоб взять новый заказ", reply_markup=new_us)
    for i in admins_lib:
        bot.send_message(i, "Заказ уже взяли\n"
                            "Когда появится новый, нажмите 'Взять заказ' ")
    bot.register_next_step_handler(send, admin)


@bot.message_handler(commands=['start', 'Start'])
def say_hello(message):
    new_us = telebot.types.ReplyKeyboardMarkup(True)
    new_us.row('Я старше 18 лет', 'Мне меньше 18 лет')
    send = bot.send_message(message.chat.id, "Рады приветствовать вас в HookahSputnik\n"
                                             "Реализация табачной продукции разрешается для лиц старшее 18 лет.\n"
                                             "Пожалуйста, подтвердите свой возраст\n"
                                             "Процесс заполнения заказа можно прервать командой /start",
                            reply_markup=new_us)

    bot.register_next_step_handler(send, hookah_sort)


def hookah_sort(message):
    if message.text == "Я старше 18 лет":
        sort_changer = telebot.types.ReplyKeyboardMarkup(True, True, True)
        sort_changer.row(sorts[0], sorts[1], sorts[2])
        send = bot.send_message(message.chat.id, "Пожалуйста, выберите вид кальяна из предоженных",
                                reply_markup=sort_changer)
        bot.register_next_step_handler(send, tobacco_sort)

    elif message.text == "Мне меньше 18 лет":
        again = telebot.types.ReplyKeyboardMarkup(True)
        again.row('/start')
        send = bot.send_message(message.chat.id, "Пожалуйста, дождитесь пока вам исполнится 18 лет "
                                                 "и возвращайтесь к нам снова",
                                reply_markup=again)
        bot.register_next_step_handler(send, say_hello)
    elif message.text == '/adm1n':
        again = telebot.types.ReplyKeyboardMarkup(True)
        again.row('Продолжить')
        send = bot.send_message(message.chat.id, "Нажмите продолжить для вход в меню исполнителя", reply_markup=again)
        bot.register_next_step_handler(send, admin)
    else:
        again = telebot.types.ReplyKeyboardMarkup(True)
        again.row('/start')
        send = bot.send_message(message.chat.id, "Неизвестное сообщение, пожалуйста начните оформление сначала",
                                reply_markup=again)
        bot.register_next_step_handler(send, say_hello)


def tobacco_sort(message):
    if message.text == '/start':
        bot.register_next_step_handler(message, say_hello)
    else:
        order = [message.text]
        tobacco_changer = telebot.types.ReplyKeyboardMarkup(True)
        tobacco_changer.row(tobacco_sorts[0], tobacco_sorts[1], tobacco_sorts[2])
        tobacco_changer.row(tobacco_sorts[3], tobacco_sorts[4], tobacco_sorts[5])
        send = bot.send_message(message.chat.id, "Выберите вид табака",
                                reply_markup=tobacco_changer)
        bot.register_next_step_handler(send, lambda msg: hookah_taste(msg, order))


def hookah_taste(message, order):
    if message.text == '/start':
        bot.register_next_step_handler(message, say_hello)
    else:
        order.append(message.text)
        taste_changer = telebot.types.ReplyKeyboardMarkup(True)
        taste_changer.row(tastes[0], tastes[1], tastes[2])
        taste_changer.row(tastes[3], tastes[4], tastes[5])
        send = bot.send_message(message.chat.id, "Выберите вкус табака",
                                reply_markup=taste_changer)
        bot.register_next_step_handler(send, lambda msg: reg_info_step(msg, order))


def reg_info_step(message, order):
    if message.text == '/start':
        bot.register_next_step_handler(message, say_hello)
    else:
        order.append(message.text)
        bot.send_message(message.chat.id, "Пожалуйста, проверьте свой заказ заказ:"
                                          "Вид кальяна: {ord}\n"
                                          "Табак: {tobacco}\n"
                                          "Вкус: {taste}".format(ord=order[0], tobacco=order[1], taste=order[2]))
        send = bot.send_message(message.chat.id, "Пожалуйста, введите информацию о себе и адресе доставки, в формате:\n"
                                                 "Имя\n"
                                                 "Номер телефона\n"
                                                 "Полный адрес доставки")
        bot.register_next_step_handler(send, lambda msg: offer_acceptance(msg, order))


def offer_acceptance(message, order):
    if message.text == '/start':
        bot.register_next_step_handler(message, say_hello)
    else:
        client_info = message.text.split("\n")
        acception = telebot.types.ReplyKeyboardMarkup(True)
        acception.row("Подтвердить", "Отклонить")
        send = bot.send_message(message.chat.id, "Подтверждая правила публичной оферты вы завершаете "
                                                 "оформление заказа и соглашаетесь с правилами оферты",
                                reply_markup=acception)
        bot.register_next_step_handler(send, lambda msg: prelast_step(msg, order, client_info))


def prelast_step(message, order, client_info):
    if message.text == "Подтвердить":
        again = telebot.types.ReplyKeyboardMarkup(True)
        again.row('/start')
        bot.send_message(message.chat.id, "Заказ сформирован и передан для выполнения"
                                          "Реквизиты для оплаты заказа: XXXX-XXXX-XXXX\n"
                                          "Также возможна оплата наличными", reply_markup=again)
        send = bot.send_message(message.chat.id, "Спасибо за оформление заказа, будем ждать вас снова",
                                reply_markup=again)
        for i in admins_lib:
            bot.send_message(i, "Поступил новый заказ:\n"
                                "Вид кальяна: {ord}\n"
                                "Табак: {tobacco}\n"
                                "Вкус: {taste}\n\n"
                                "Информация о заказчике:\n"
                                "Имя: {name}\n"
                                "Номер телефона: {number}\n"
                                "Адрес: {address}".format(ord=order[0], tobacco=order[1], taste=order[2],
                                                          name=client_info[0], number=client_info[1],
                                                          address=client_info[2]))
        bot.register_next_step_handler(send, say_hello)
    else:
        again = telebot.types.ReplyKeyboardMarkup(True)
        again.row('/start')
        send = bot.send_message(message.chat.id, "Вы не согласны с правилами публичной оферты, перевожу вас в начало",
                                reply_markup=again)
        bot.register_next_step_handler(send, say_hello)


if __name__ == "__main__":
    try:
        bot.polling(none_stop=True)
    except Exception as e:
        print(e)
        bot.stop_polling()
