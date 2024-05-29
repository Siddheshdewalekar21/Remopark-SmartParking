from django.contrib import admin
from .models import CustomUser, ParkingCenter, ParkingSpot, BookedSpot,CapturedImage    

# class CustomUserAdmin(admin.ModelAdmin):
#     list_display = ('id', 'username', 'full_name', 'email', 'phone', 'car_number')

# class ParkingSpotAdmin(admin.ModelAdmin):
#     list_display = ('id', 'name', 'parking_center','spot_code')

# class BookedSpotAdmin(admin.ModelAdmin):
#     list_display = ('id', 'user', 'parking_spot', 'parking_center', 'spot_code')

# class ParkingCenterAdmin(admin.ModelAdmin):
#     list_display = ('name', 'price', 'latitude', 'longitude', 'contact')

# # Register your models here.
# admin.site.register(CustomUser, CustomUserAdmin)
# admin.site.register(ParkingCenter, ParkingCenterAdmin)
# admin.site.register(ParkingSpot, ParkingSpotAdmin)
# admin.site.register(BookedSpot, BookedSpotAdmin)

admin.site.register(CapturedImage)
admin.site.register(ParkingSpot)
admin.site.register(BookedSpot)
admin.site.register(ParkingCenter)
admin.site.register(CustomUser)