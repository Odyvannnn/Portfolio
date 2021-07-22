from sqlalchemy import create_engine
from sqlalchemy.exc import InvalidRequestError
from sqlalchemy.orm import sessionmaker

from database.models import Base, User


class Database:
    def __init__(self, obj):
        engine = create_engine(obj, echo=False, connect_args={'check_same_thread': False})
        Session = sessionmaker(bind=engine)
        self.session = Session()
        Base.metadata.create_all(bind=engine)

    def add(self, obj):
        try:
            self.session.add(obj)
            self.session.commit()
        except InvalidRequestError:
            self.session.rollback()

    def roll_back(self):
        return self.session.rollback()

    def add_bill_by_chat_id(self, message):
        text = self.get_status_by_chat_id(message.chat.id)
        text.billed = True
        self.session.commit()

    def add_express_by_chat_id(self, message):
        text = self.get_status_by_chat_id(message.chat.id)
        text.express = True
        self.session.commit()

    def add_express_to_users(self):
        orders = self.get_users_for_express()
        for o in range(len(orders)):
            text = self.get_user_for_express()
            text.billed = False
            self.session.commit()

    def add_bills_to_users(self):
        orders = self.get_users_for_bill()
        for o in range(len(orders)):
            text = self.get_user_for_bill()
            text.billed = False
            self.session.commit()

    def get_users_for_bill(self):
        return self.session.query(User).filter(User.billed == True).all()

    def get_user_for_bill(self):
        return self.session.query(User).filter(User.billed == True).first()

    def get_users_from_user(self):
        return self.session.query(User).all()

    def get_status_by_chat_id(self, chat_id):
        return self.session.query(User).filter(User.chat_number == chat_id).first()

    def get_users_for_express(self):
        return self.session.query(User).filter(User.express == True).all()

    def get_user_for_express(self):
        return self.session.query(User).filter(User.express == True).first()
