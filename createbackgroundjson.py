"""
this file is a simple json generator for me because iim to lazy to type out 20 different json strings
"""

import json
from json import decoder
from os import write 
import configparser
from re import I 

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
    print("Starting to Open file for writing")
    while id < 26:
        data = {'id':id,'image':str(basedir)+str(id)+str(fileextention),'sp_posX': addToFile(id,"sp_posX"), 'sp_posY':addToFile(id,"sp_posY"),'mp_posX':addToFile(id,"mp_posX"),'mp_posY':addToFile(id,"mp_posY"),'settings_posX':addToFile(id,"settings_posX"),
         'settings_posY':0, 'quit_posX':0, 'quit_posY':0,'splash_posX':0, 'splash_posY':0,'edition_posX':0,'edition_posY':0, 'lang_posX':0, 'lang_posY':0}
        print(json.dumps(data, sort_keys=True, indent=4))

        output.append(data)
        id +=1
        if(id == 26):
            with open('src/main/resources/assets/minecraft/blackburn/backgrounds.json', 'a', encoding='utf8') as outfile:
                print(json.dumps(data, sort_keys=True, indent=4))
                json.dump(output, outfile, sort_keys=True, indent=4)

# adds keys that are missing into file 
def addToFile(id,key):
   
    section = "sect" + str(id) 
    out = section.strip("\"")

    print("Config Sections"+ " "+ str(readConfig(id)[out]))
    print("Config data"+ " "+ str(readConfig(id)[out][str(key)]))

    return readConfig(id)[out][str(key)]

    
    

        
    
                    
            
            




addToFile()