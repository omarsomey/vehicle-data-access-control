import requests
from requests.auth import HTTPBasicAuth

import json

from pathlib import Path

from datetime import datetime

session_time = datetime.now()
Path(f"data_dump/{session_time.strftime('%m_%d_%Y-%H_%M_%S')}").mkdir(parents=True, exist_ok=True)

url = "http://localhost:8080/info"

while True:
    res = requests.get(url, auth=HTTPBasicAuth('simon', 'demo'))
    res_json = res.json()

    time = datetime.now()

    with open(f"data_dump/{session_time.strftime('%m_%d_%Y-%H_%M_%S')}/data_{time.microsecond}.json", 'w') as outfile:
        json.dump(res_json, outfile)



