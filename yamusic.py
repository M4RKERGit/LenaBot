import os
import sys
import json
from youtubesearchpython import *
import pafy
import eyed3 
from moviepy.editor import *

if os.path.exists('res.mp4'):
    os.remove('res.mp4')

if os.path.exists('res_sound.mp3'):
    os.remove('res_sound.mp3')

music = ""
for i in sys.argv:
    if sys.argv[0] == i:
        continue
    music = music + i + " "
videosSearch = VideosSearch(music, limit = 1, region = 'RU')
todos = json.loads(videosSearch.result( mode = ResultMode.json))
video = pafy.new(todos['result'][0]['link'])
print(todos['result'][0]['duration'])
print(todos['result'][0]['link'])
nm = video.title
print(video.title)

naming = nm.split("-")
 
if len(todos['result'][0]['duration']) <= 5:
    temp = todos['result'][0]['duration'].split(":",1)
    print(temp)
    if int(temp[0]) <= 15:
        bestaudio = video.getbest(preftype="mp4")
        bestaudio.download("res.mp4")
        video = VideoFileClip("res.mp4")
        video.audio.write_audiofile("res_sound.mp3")

        audiofile = eyed3.load("res_sound.mp3")
        audiofile.tag.title = naming[1].strip(" ")
        audiofile.tag.artist = naming[0].strip(" ")

        audiofile.tag.save()