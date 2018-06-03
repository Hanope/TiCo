from django.urls import path
from . import views

urlpatterns = [
    path(r'timetable', views.timetable),
]