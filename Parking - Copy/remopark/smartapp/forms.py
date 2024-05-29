from django import forms
from django.contrib.auth.forms import UserCreationForm
from .models import CustomUser, ParkingCenter, ParkingSpot, BookedSpot,ContactMessage

class CustomUserForm(UserCreationForm):
    class Meta:
        model = CustomUser
        fields = ['username', 'email', 'full_name', 'phone', 'car_number', 'password1', 'password2']
        help_texts = {
            'username': None,
            'email': None,
            'full_name': None,
            'phone': None,
            'car_number': None,
            'password1': None,
            'password2': None
        }
        
class ParkingCenterForm(forms.ModelForm):
    class Meta:
        model = ParkingCenter
        fields = ['name']

class ParkingSpotForm(forms.ModelForm):
    class Meta:
        model = ParkingSpot
        fields = ['name', 'parking_center']

class BookedSpotForm(forms.ModelForm):
    class Meta:
        model = BookedSpot
        fields = ['user', 'parking_spot', 'parking_center', 'spot_code']

class BookingForm(forms.Form):
    available_slots = forms.MultipleChoiceField(choices=[], widget=forms.CheckboxSelectMultiple(attrs={'class': 'slot-checkbox'}))

    def __init__(self, *args, **kwargs):
        slot_info = kwargs.pop('slot_info')
        super(BookingForm, self).__init__(*args, **kwargs)
        self.fields['available_slots'].choices = [(str(key), value) for key, value in slot_info.items()]
        
class ContactForm(forms.ModelForm):
    class Meta:
        model = ContactMessage
        fields = ['name', 'email', 'message']
        widgets = {
            'message': forms.Textarea(attrs={'rows': 5}),
        }
