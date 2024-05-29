from . import views
from django.urls import path
from django.shortcuts import render, redirect

urlpatterns = [
    path('', views.home, name='home'),
    path('map', views.map, name='map'),
    path('centers/<int:center_id>/', views.show_centers, name='empty_spots'),
    path('spot/<int:spot_id>/', views.show_spot, name='spot_details'),  
    path('confirmation/', views.confirmation, name='confirmation'), 
    path('success', views.success, name='success'),
    path('destination', views.destination, name='destination'),
    path('deleteall', views.deleteAll, name='deleteAll'),
    path('signup', views.signup, name='signup'),
    path('about', views.about, name='about'),
    path('services', views.services, name='services'),
    path('profile/', views.profile, name='profile'),
    path('logout', views.user_logout, name='logout'),
    path('login', views.user_login, name='login'),
    path('contact', views.contact, name='contact'),
    path('camera', views.capture_image, name='camera'),
    path('capture_image/', views.capture_image, name='capture_image'),
    path('upload_image/', views.upload_image, name='upload_image'),
    path('display_image/', views.display_image, name='display_image'),
    path('delete_past/', views.delete_past_bookings, name='delete_past_bookings'),
]
