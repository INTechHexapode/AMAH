#!/bin/bash
LOGIN=pi
DOSSIER_DESTINATION=/home/pi/hexapode/software/java/

rsync -e ssh --delete-after --exclude-from exclusion.txt -az ../ "$LOGIN"@"$1":"$DOSSIER_DESTINATION"
