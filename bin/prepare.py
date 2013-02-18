#!/usr/bin/env python
import os
import zipfile

def zipdir(path, zip):
    for root, dirs, files in os.walk(path):
        for file in files:
            if not file.lower().endswith(("jar","zip")):
                print("adding file:" + file)
                zip.write(os.path.join(root, file),file)

if __name__ == '__main__':
    zip = zipfile.ZipFile('survivalchests.jar', 'w')
    usefolder = os.path.dirname(os.path.realpath(__file__))
    zipdir(usefolder, zip)
    zip.close()