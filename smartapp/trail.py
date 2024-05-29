import cv2
import pickle
import numpy as np
width, height = 175, 224
def camera(): 
    img = cv2.imread('media/parking_spot_images/park.jpg') 
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
    return green_counters 
    
print(camera())

