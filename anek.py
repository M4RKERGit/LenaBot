from bs4 import BeautifulSoup
import requests
from gtts import gTTS
#from playsound import playsound
import sys, os

var = sys.argv[1]

url = 'https://baneks.ru/' +  var
page = requests.get(url)
soup = BeautifulSoup(page.content, "html.parser")
genres = soup.find('p').getText()
tts = gTTS(genres, lang="ru", slow=False)
if os.path.exists('tts_output.ogg'):
    os.remove('tts_output.ogg')
tts.save('tts_output.ogg')



#playsound('tts_output.mp3');