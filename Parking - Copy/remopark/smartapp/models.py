from django.db import models
from django.contrib.auth.models import AbstractUser

class ParkingCenter(models.Model):
    name = models.CharField(max_length=100)
    price = models.IntegerField()
    latitude = models.FloatField()
    longitude = models.FloatField()
    contact = models.CharField(max_length=15) 
    def __str__(self):
        return self.name

class ParkingSpot(models.Model):
    name = models.CharField(max_length=100)
    parking_center = models.ForeignKey(ParkingCenter, on_delete=models.CASCADE)
    spot_code = models.CharField(max_length=50, default='z')

    def __str__(self):
        return f"{self.name} at {self.parking_center.name}"
    
class CustomUser(AbstractUser):
    full_name = models.CharField(max_length=150)
    email = models.EmailField(unique=True)
    phone = models.CharField(max_length=15)
    car_number = models.CharField(max_length=20)

    def __str__(self):
        return self.username

class BookedSpot(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    parking_spot = models.ForeignKey(ParkingSpot, on_delete=models.CASCADE)
    parking_center = models.ForeignKey(ParkingCenter, on_delete=models.CASCADE)
    spot_code = models.CharField(max_length=50)

    def __str__(self):
        return f"{self.user.username} booked {self.parking_spot.name} ({self.spot_code}) at {self.parking_center.name}"
    
class ContactMessage(models.Model):
    name = models.CharField(max_length=100)
    email = models.EmailField()
    message = models.TextField()

    def __str__(self):
        return self.subject

class CapturedImage(models.Model):
    image = models.ImageField(upload_to='parking_spot_images/')
    timestamp = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Image captured at {self.timestamp}"