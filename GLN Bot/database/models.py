from sqlalchemy import Column, Integer, Boolean
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()


class User(Base):
    __tablename__ = "user"
    id = Column(Integer, primary_key=True, autoincrement=True)
    chat_number = Column(Integer, nullable=False, unique=True)
    billed = Column(Boolean)
    express = Column(Boolean)

    def __init__(self, chat_number, billed, express):
        self.chat_number = chat_number
        self.billed = billed
        self.express = express

    def __repr__(self):
        return "User(%s, %s, %s)" % (self.chat_number, self.billed, self.express)

