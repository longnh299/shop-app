from mongoengine import connect, Document, StringField

connect('student',
        host="mongodb",
        port=27017,
        username="longnh",
        password="practice",
        authentication_source='admin')


class University(Document):
    name = StringField(required=True, unique=True)
