"""
this file is a simple json generator for me because iim to lazy to type out 20 different json strings
"""

import json
from json import decoder
from os import write 
import configparser
from re import I 
import os 
from pathlib import Path
id = 0
fileid = 0

basedir = "blackburn/background/"
fileextention = ".png"

output = []

def readConfig(i):
    
    config = configparser.ConfigParser()
    config.read('src/main/resources/assets/minecraft/blackburn/tool/mainmenu.ini')
    return config

# creates basic json structurie
def create_baseFile(id,basedir,fileextention):
    if(os.path.exists('src/main/resources/assets/minecraft/blackburn/backgrounds.json')):
        os.remove('src/main/resources/assets/minecraft/blackburn/backgrounds.json')
    else:

        print("Starting to Open file for writing")
        
        while id < 26:
            data = {'id':id,'image':str(basedir)+str(id)+str(fileextention),'buttonLenght':addToFile(id,"buttonLength"),'sp_posX': addToFile(id,"sp_posX"), 'sp_posY':addToFile(id,"sp_posY"),'mp_posX':addToFile(id,"mp_posX"),'mp_posY':addToFile(id,"mp_posY"),'settings_posX':addToFile(id,"settings_posX"),
            'settings_posY':addToFile(id,"settings_posY"), 'quit_posX':addToFile(id,"quit_posX"), 'quit_posY':addToFile(id,"quit_posY"),'splash_posX':addToFile(id,"splash_posX"), 
            'splash_posY':addToFile(id,"splash_posY"),'splash_rot':addToFile(id,"splash_rot"),'edition_posX':addToFile(id,"edition_posX"),'edition_posY':addToFile(id,"edition_posY"),'edition_posXNonFull':addToFile(id,"edition_posX_nonfull"),'edition_posy_nonfull':addToFile(id,"edition_posy_nonfull"),
            'lang_posX':addToFile(id,"lang_posX"), 'lang_posY':addToFile(id,"lang_posY")}
        
            output.append(data)
            id +=1

            if(id == 26):
                with open('src/main/resources/assets/minecraft/blackburn/backgrounds.json', 'a', encoding='utf8') as outfile:
                    print(json.dumps(data, sort_keys=False, indent=4))
                    json.dump(output, outfile, sort_keys=False, indent=4)
            
# adds keys that are missing into file 
def addToFile(id,key):
   
    section = "sect" + str(id) 
    out = section.strip("\"")
    return readConfig(id)[out][str(key)]
    
    

        
    
                    
            
            




create_baseFile(id,basedir,fileextention)