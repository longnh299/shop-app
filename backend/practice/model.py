from mongoengine import connect, Document, StringField, ReferenceField, IntField, EnumField, \
    DateTimeField, ListField

from common.model import Status
from intern.model import Intern

connect('student',
        host="mongodb",
        port=27017,
        username="longnh",
        password="practice",
        authentication_source='admin')


class Submission(Document):
    intern = ReferenceField(Intern, required=True)
    grade = IntField(min_value=0, max_value=10)
    status = EnumField(Status, default=Status.ON_TIME)


class Practice(Document):
    name = StringField(required=True)
    deadline = DateTimeField(required=True)
    submissions = ListField(ReferenceField(Submission), default=[])
