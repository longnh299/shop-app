import datetime
import random

import pytz

from common.model import Status
from intern.model import Intern
from lecture.model import Participating, Lecture
from mentor.model import Mentor
from practice.model import Submission, Practice
from university.model import University


def generate_participating():
    interns_list = Intern.objects.all()
    participatings = []
    for intern in interns_list:
        participating = Participating.objects.create(intern=intern, status=random.choice(list(Status)))
        participating.save()
        participatings.append(participating)
    return participatings


def generate_submission():
    interns_list = Intern.objects.all()
    submissions = []
    for intern in interns_list:
        submission = Submission.objects.create(intern=intern, grade=random.randint(0, 10),
                                               status=random.choice(list(Status)))
        submission.save()
        submissions.append(submission)
    return submissions


def ref_from_universities(name):
    return University.objects(name=name).first().to_dbref()


def ref_from_mentors_list(mentors):
    if len(mentors) == 0:
        return [mentor.to_dbref() for mentor in Mentor.objects.all()]
    return [Mentor.objects(name=mentor).first().to_dbref() for mentor in mentors]


def init():
    University.objects.delete({})
    Intern.objects.delete({})
    Mentor.objects.delete({})
    Lecture.objects.delete({})

    University.objects.insert([
        University(name="ĐH Bách khoa Hà Nội"),
        University(name="ĐH GTVT"),
        University(name="ĐH KHTN-VNUHCM"),
        University(name="ĐH Kinh tế Quốc dân"),
        University(name="Học viện Kỹ thuật Mật mã"),
        University(name="ĐH Công nghệ - ĐH Quốc gia Hà Nội"),
        University(name="HV BCVT"),
    ])

    Intern.objects.insert([
        Intern(order=1, name="Nguyễn Hoàng Long", year_of_birth=2000,
               university=ref_from_universities("ĐH Bách khoa Hà Nội"),
               major="Kỹ thuật máy tính", gender="nam"),

        Intern(order=2, name="Hoang Long Nguyen", year_of_birth=2002,
               university=ref_from_universities("ĐH Bách khoa Hà Nội"),
               major="Khoa học máy tính", gender="nam"),

        Intern(order=3, name="Longnh", year_of_birth=2000,
               university=ref_from_universities("ĐH Công nghệ - ĐH Quốc gia Hà Nội"),
               major="Tự động hóa", gender="nam"),

        Intern(order=4, name="Đặng Thúy Anh", year_of_birth=2001,
               university=ref_from_universities("ĐH Kinh tế Quốc dân"),
               major="Marketing", gender="nữ"),

        Intern(order=5, name="Lê Mỹ Hoa", year_of_birth=2001,
               university=ref_from_universities("ĐH KHTN-VNUHCM"),
               major="Công nghệ thông tin", gender="nữ"), 

        Intern(order=6, name="Ivan Ipatov", year_of_birth=2000,
               university=ref_from_universities("ĐH Bách khoa Hà Nội"),
               major="Information Security", gender="nam"),

        Intern(order=7, name="Nguyễn Nguyễn", year_of_birth=2000,
               university=ref_from_universities("HV BCVT"),
               major="Điện tử viễn thông", gender="nam"),

        Intern(order=8, name="Lee Chong Wei", year_of_birth=2002,
               university=ref_from_universities("Học viện Kỹ thuật Mật mã"),
               major="Điện tử viễn thông", gender="nam"),

        Intern(order=9, name="Hoàng Lan", year_of_birth=2000,
               university=ref_from_universities("ĐH GTVT"),
               major="Logistic", gender="nữ"),

    ])

    Mentor.objects.insert([
        Mentor(name="ThanhLX"),
        Mentor(name="TienND"),
        Mentor(name="ThanhNT"),
        Mentor(name="ThuNQ"),
    ])

    Lecture.objects.insert([
        Lecture(order=1, name="Computer Network",
                mentors=ref_from_mentors_list(["ThuNQ"]),
                started_at=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 4, 24, 8, 30)),
                participatings=generate_participating()),
        Lecture(order=2, name="Computer Architecture",
                mentors=ref_from_mentors_list(["TienND"]),
                started_at=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 4, 24, 13, 30)),
                participatings=generate_participating()),
        Lecture(order=3, name="Programming Technology",
                mentors=ref_from_mentors_list(["ThanhNT"]),
                started_at=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 5, 7, 8, 30)),
                participatings=generate_participating()),
        Lecture(order=4, name="Introduction to Security",
                mentors=ref_from_mentors_list(["ThanhLX"]),
                started_at=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 5, 7, 13, 30)),
                participatings=generate_participating())
    ])

    Practice.objects.insert([
        Practice(name="Java OOP",
                 deadline=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 4, 24, 8, 30)),
                 submissions=generate_submission()),
        Practice(name="Learn about TCP/IP model",
                 deadline=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 4, 24, 8, 30)),
                 submissions=generate_submission()),
        Practice(name="SQL Injection practice",
                 deadline=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 4, 24, 8, 30)),
                 submissions=generate_submission()),
        Practice(name="Practice 4",
                 deadline=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 4, 24, 8, 30)),
                 submissions=generate_submission()),
        Practice(name="Practice 5",
                 deadline=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 4, 24, 8, 30)),
                 submissions=generate_submission()),
        Practice(name="Practice 6",
                 deadline=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 4, 24, 8, 30)),
                 submissions=generate_submission()),
        Practice(name="Practice 7",
                 deadline=pytz.timezone('Asia/Ho_Chi_Minh').localize(datetime.datetime(2023, 4, 24, 8, 30)),
                 submissions=generate_submission()),
    ])

    return ""
