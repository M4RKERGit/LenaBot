import os, sys, image_search

if os.path.exists('picture.jpg'):
    os.remove('picture.jpg')
    
image_search google cat --limit 10 --json