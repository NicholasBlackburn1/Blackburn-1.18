"""
this file is a simple json generator for me because iim to lazy to type out 20 different json strings
"""

import json
from os import write 

id = 0
fileid = 0

basedir = "blackburn/background/"
fileextention = ".png"

output = []


while id < 26:
    data = {'id':id,'image':str(basedir)+str(id)+str(fileextention),'posX': 0, 'posY':0}
    print(json.dumps(data, sort_keys=True, indent=4))

    output.append(data)
    id +=1
    if(id == 26):
        with open('src/main/resources/assets/minecraft/blackburn/backgrounds.json', 'a', encoding='utf8') as outfile:
            print(json.dumps(data, sort_keys=True, indent=4))
            json.dump(output, outfile, sort_keys=True, indent=4)