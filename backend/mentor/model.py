from mongoengine import connect, Document, StringField

connect('student',
        host="mongodb",
        port=27017,
        username="longnh",
        password="practice",
        authentication_source='admin')


class Mentor(Document):
    name = StringField(required=True, unique=True)
