from django.shortcuts import render, get_object_or_404, redirect
from django.core.mail import send_mail
from django.conf import settings
from django.http import JsonResponse
from django.utils import timezone
import datetime,json,pytz
from datetime import date
from .forms import BookingForm, CustomUserForm
from .models import ParkingCenter, ParkingSpot,BookedSpot ,CustomUser,CapturedImage
from django.contrib.auth.decorators import login_required
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth import authenticate, login,logout
import cv2,pickle,base64,time,numpy as np
from django.http import JsonResponse
from django.core.files.base import ContentFile
from django.views.decorators.csrf import csrf_exempt

time_slots = {
    'a': '00:00 AM - 01:00 AM',
    'b': '01:00 AM - 02:00 AM',
    'c': '02:00 AM - 03:00 AM',
    'd': '03:00 AM - 04:00 AM',
    'e': '04:00 AM - 05:00 AM',
    'f': '05:00 AM - 06:00 AM',
    'g': '06:00 AM - 07:00 AM',
    'h': '07:00 AM - 08:00 AM',
    'i': '08:00 AM - 09:00 AM',
    'j': '09:00 AM - 10:00 AM',
    'k': '10:00 AM - 11:00 AM',
    'l': '11:00 AM - 12:00 PM',
    'm': '12:00 PM - 01:00 PM',
    'n': '01:00 PM - 02:00 PM',
    'o': '02:00 PM - 03:00 PM',
    'p': '03:00 PM - 04:00 PM',
    'q': '04:00 PM - 05:00 PM',
    'r': '05:00 PM - 06:00 PM',
    's': '06:00 PM - 07:00 PM',
    't': '07:00 PM - 08:00 PM',
    'u': '08:00 PM - 09:00 PM',
    'v': '09:00 PM - 10:00 PM',
    'w': '10:00 PM - 11:00 PM',
    'x': '11:00 PM - 12:00 AM'
}

def show_centers(request, center_id):
    center = get_object_or_404(ParkingCenter, id=center_id)
    parking_spots = center.parkingspot_set.all()
    context = {
        'center': center,
        'empty_spots': parking_spots,   
    }
    return render(request, 'centers.html', context)

def get_filtered_time_slots(spot_code):
    newlist = [char for char in spot_code]
    current_key = get_current_key()
    filtered_time_slots = {key: value for key, value in time_slots.items() 
                           if key not in newlist and key > current_key}
    return filtered_time_slots

def show_spot(request, spot_id):
    spot = get_object_or_404(ParkingSpot, id=spot_id)
    spot_code = spot.spot_code
    center = spot.parking_center
    price=center.price
    if request.method == 'POST':
        form = BookingForm(request.POST, slot_info=get_filtered_time_slots(spot_code))
        if form.is_valid():
            selected_slots = form.cleaned_data['available_slots']
            spots = ''.join(selected_slots)
            updatedSpotcode = spot_code + spots
            print(spots)
            print(updatedSpotcode)
            request.session['selected_slots']=spots  
            request.session['parking_spot_id']=spot_id  
            request.session['parking_center_id']=center.id 
            request.session['center']=center.name   
            request.session['spotname'] = spot.name
            request.session['updatedSlots'] = updatedSpotcode
            request.session['price'] = price
            return redirect('confirmation')
    else:
        form = BookingForm(slot_info=get_filtered_time_slots(spot_code))
        
    context = {
        'spot': spot,
        'form': form,
    }
    return render(request, 'spot.html', context)

def confirmation(request):
    times_list=[]
    selected_slots=request.session.get('selected_slots')
    spotname=request.session.get('spotname')
    price=request.session.get('price')
    center = request.session.get('center')
    selected_slots=request.session.get('selected_slots')
    for i in selected_slots:
        times_list.append(time_slots[i])
    request.session['slots_list'] = times_list
    bill = (price * len(times_list))*100
    razorpayBill=bill *100
    today = date.today()
    return render(request, 'confirmation.html', {'name': spotname,'spots':times_list,'center':center,'bill':bill,'date':today,'razorpayBill':razorpayBill})

def get_current_key():
    time_slots = {
        'a': ('00:00', '01:00'),
        'b': ('01:00', '02:00'),
        'c': ('02:00', '03:00'),
        'd': ('03:00', '04:00'),
        'e': ('04:00', '05:00'),
        'f': ('05:00', '06:00'),
        'g': ('06:00', '07:00'),
        'h': ('07:00', '08:00'),
        'i': ('08:00', '09:00'),
        'j': ('09:00', '10:00'),
        'k': ('10:00', '11:00'),
        'l': ('11:00', '12:00'),
        'm': ('12:00', '13:00'),
        'n': ('13:00', '14:00'),
        'o': ('14:00', '15:00'),
        'p': ('15:00', '16:00'),
        'q': ('16:00', '17:00'),
        'r': ('17:00', '18:00'),
        's': ('18:00', '19:00'),
        't': ('19:00', '20:00'),
        'u': ('20:00', '21:00'),
        'v': ('21:00', '22:00'),
        'w': ('22:00', '23:00'),
        'x': ('23:00', '00:00')
    }

    india_tz = pytz.timezone('Asia/Kolkata')
    current_time = india_tz.localize(datetime.datetime.now()).time()

    for key, time_range in time_slots.items():
        start_time, end_time = [datetime.datetime.strptime(t, '%H:%M').time() for t in time_range]
        if start_time <= current_time < end_time:
            return key
     
def map(request):
    parking_centers = ParkingCenter.objects.all().values('id','name', 'price', 'latitude', 'longitude', 'contact')
    parking_centers_json = json.dumps(list(parking_centers))

    context = {
        'parking_centers': parking_centers_json
    }

    return render(request, 'map.html', context)

def success(request):
    booked_times=""
    parking_spot_id=request.session.get('parking_spot_id')
    parking_center_id=request.session.get('parking_center_id')
    updatedSlots=request.session.get('updatedSlots')
    selected_slots=request.session.get('selected_slots') 
    user = get_object_or_404(CustomUser, id=request.user.id)
    for slot_code in selected_slots:
        booked_spot = BookedSpot(
            user=user,
            parking_spot=ParkingSpot.objects.get(id=parking_spot_id),
            parking_center=ParkingCenter.objects.get(id=parking_center_id),
            spot_code=slot_code
        )
        booked_times+=(time_slots[slot_code])
    
    spot = get_object_or_404(ParkingSpot, id=parking_spot_id)
    spot.spot_code = str(updatedSlots)
    booked_spot.save()
    spot.save()
    subject = 'Your parking spot is booked!'
    message = f'Dear {user.username},\n\nYour parking spot at {spot.parking_center.name} has been booked successfully.\n\nSpot Details:\nSpot Name: {spot.name}\Time : {booked_times}\n\nThank you for using our service!'
    from_email = settings.DEFAULT_FROM_EMAIL
    recipient_list = [user.email]
    send_mail(subject, message, from_email, recipient_list, fail_silently=False)
    request.session.clear()
    return render(request,'success.html')

@login_required(login_url='login')
def home(request):
    user = request.user
    booked_spots = BookedSpot.objects.filter(user=user)
    time_slots_list = list(time_slots.items())
    return render(request, 'home.html', {'booked_spots': booked_spots, 'time_slots': time_slots_list})   

def destination(request):
    return render(request,'destination.html')

def delete_past_bookings():
    current_key = get_current_key()
    if current_key:
        BookedSpot.objects.filter(spot_code__lt=current_key).delete()

def deleteAll(request):
    if request.method == 'GET':
        utc_now = timezone.now()
        ist = pytz.timezone('Asia/Kolkata')
        now = utc_now.astimezone(ist)
        print(now.hour)
        if now.hour == 0:  
            spots = ParkingSpot.objects.all()
            for spot in spots:
                spot.spot_code ='z' 
                spot.save()
            BookedSpot.objects.all().delete()
            return JsonResponse({'message': 'Spot codes replaced with z and Bookspot Entries deleted'}, status=200)
        else:
            return JsonResponse({'error': 'Task can only be executed at 12:00'}, status=400)
    else:
        return JsonResponse({'error': 'Invalid request method'}, status=400)
    
def signup(request):
    if request.method == 'POST':
        form = CustomUserForm(request.POST)
        if form.is_valid():
            form.save()
            username = form.cleaned_data.get('username')
            password = form.cleaned_data.get('password1')
            user = authenticate(username=username, password=password)
            return redirect('home')
    else:
        form = CustomUserForm()
    return render(request, 'signup.html', {'form': form})

def user_login(request):
    if request.method == 'POST':
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(request, username=username, password=password)
        if user is not None:
            login(request, user)
            return redirect('home')
        else:
            return render(request, 'login.html', {'error': 'Invalid login credentials'})
    else:
        return render(request, 'login.html')
    
def user_logout(request):
    logout(request)
    return redirect('login') 

def contact(request):
    return render(request,'contact.html')
def about(request):
    return render(request,'about.html')
def services(request):
    return render(request,'services.html')
def profile(request):
    return render(request,'profile.html')

width, height = 175, 224

def camera(): 
    img = cv2.imread('media/parking_spot_images/parking.jpg') 
    imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY) 
    imgBlur = cv2.GaussianBlur(imgGray, (3, 3), 1) 
    imgThreshold = cv2.adaptiveThreshold(imgBlur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 25, 16) 
    imgMedian = cv2.medianBlur(imgThreshold, 5)
    kernel = np.ones((3, 3), np.uint8)
    imgDilate = cv2.dilate(imgMedian, kernel, iterations=1) 
    with open('CarParkPos1', 'rb') as f:
        posList = pickle.load(f) 
    green_counters = [] 
    for pos_entry in posList: 
        if isinstance(pos_entry, tuple): 
            pos, counter = pos_entry    
            if isinstance(pos, tuple): 
                x, y = pos 
                imgCrop = imgDilate[y:y + height, x:x + width] 
                count = cv2.countNonZero(imgCrop) 
                if count < 2500:    
                    green_counters.append(counter)
    print(green_counters)
    checking(green_counters) 
 
        
def capture_image(request):
    return render(request, 'capture.html')

@csrf_exempt
def upload_image(request):
    if request.method == 'POST':
        CapturedImage.objects.all().delete()
        data = request.POST.get('image')
        format, imgstr = data.split(';base64,')
        ext = format.split('/')[-1]
        image_data = ContentFile(base64.b64decode(imgstr), name="parking.jpg")
        parking_spot_image = CapturedImage(image=image_data)
        parking_spot_image.save()
        camera()


def display_image(request):
    latest_image = CapturedImage.objects.latest('timestamp')
    india_tz =pytz.timezone('Asia/Kolkata')
    latest_image.timestamp = latest_image.timestamp.astimezone(india_tz)
    context = {'image': latest_image}
    return render(request, 'display.html', context)


def checking(spot_names_to_exclude):
    spots = set(['1','2', '3','4', '5', '6'])
    remaining_spots = spots - set(spot_names_to_exclude)
    spot_names = ['A' + str(num) for num in remaining_spots]
    all_parking_spots = ParkingSpot.objects.filter(name__in=spot_names)
    currentKey = get_current_key()
    prevKey=chr(ord(currentKey) - 1)
    for spot in all_parking_spots:
        if currentKey not in spot.spot_code:
            booked_spots = BookedSpot.objects.filter(parking_spot=spot, spot_code__contains=prevKey).values_list('user__email', flat=True)
            subject = 'Time Extended Please pay Bill'
            message = rf'Dear {user.username},\n\nYour parking spot at {spot.parking_center.name} has been booked successfully.\n\nSpot Details:\nSpot Name: {spot.name}\Time : {booked_times}\n\nThank you for using our service!'
            from_email = settings.DEFAULT_FROM_EMAIL
            recipient_list = booked_spots
            send_mail(subject, message, from_email, recipient_list, fail_silently=False)
