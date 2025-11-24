from peewee import *
from datetime import datetime

db = SqliteDatabase(":memory:")

db.execute_sql("PRAGMA foreign_keys = ON;")

class BaseModel(Model):
    class Meta:
        database = db

class User(BaseModel):
    name = CharField(max_length=30)

class Tweet(BaseModel):
    post = CharField(max_length=248)
    user = ForeignKeyField(User, backref="tweets")

db.create_tables([User,Tweet], safe = True)

u1 = User.create(name="jaimito")
u2 = User.create(name="juanito")
u3 = User.create(name="joselito")

Tweet.create(post="ha escrito jaimito", user=u1)
Tweet.create(post="ha escrito juanito", user=u2)
Tweet.create(post="ha escrito joselito", user=u3)

query = (
    User
    .select(User.name)
    .where(User.name.contains("ni"))
)

resultado = query.execute()

for r in resultado:
    print(r.name)