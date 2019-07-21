#!/usr/bin/env python3

import flask
import json

import numpy as np # linear algebra
import pandas as pd # data processing, CSV file I/O (e.g. pd.read_csv)

import matplotlib.pyplot as plt

import torch
from torch import nn
from torch import optim
import torch.nn.functional as F
from torchvision import datasets, transforms, models

import os

# import module we'll need to import our custom module
from shutil import copyfile

# copy our file into the working directory (make sure it has .py suffix)
#copyfile(src = "view_helper.py", dst = "working/view_helper.py")

# import all our functions
import view_helper as helper

app = flask.Flask(__name__)

test_transforms=0
model=0
result=''

def init() :
    global test_transforms
    global model
    test_transforms = transforms.Compose([transforms.Resize(255),
                                      transforms.CenterCrop(224),
                                      transforms.ToTensor(),
                                      transforms.Normalize([0.485, 0.456, 0.406],
                                                           [0.229, 0.224, 0.225])])

    model = models.densenet121(pretrained=True)
    model.classifier = nn.Sequential(nn.Linear(1024, 256),
                                 nn.ReLU(),
                                 nn.Dropout(0.2),
                                 nn.Linear(256, 2),
                                 nn.LogSoftmax(dim=1))
    state_dict = torch.load('test.pth')
    model.load_state_dict(state_dict)

@app.route('/')
def index():
    return "Hello, World!"

@app.route('/result')
def res():
    return repr(result[0]) + ' ' + repr(result[1])

@app.route('/Upload/<string:name>', methods=['GET'])
def uploadFile(name):
    test_on_single()
    if (result[0] >= result[1]):
        return 'I am ' + str(int(result[0]*100)) + '% confident that there is a cat!'
    return 'I am ' + str(int(result[1]*100)) + '% confident that there is a dog!'


def download_model():
    return torch.load('test.pt')

def test_on_single():
    global result
    test_data = datasets.ImageFolder('/home/anisa/Labs/CourseWork/lab-2/Lab-1-MS/Lab-1-MS/upload-dir/', transform=test_transforms)
    trainloader = torch.utils.data.DataLoader(test_data, batch_size=64, shuffle=True)

    testloader = torch.utils.data.DataLoader(test_data, batch_size=64)
    model.to('cpu')
    model.eval()

    data_iter = iter(testloader)

    images, labels = next(data_iter)

    with torch.no_grad():
        output = model.forward(images)

    ps = torch.exp(output)
    probability = ps[0].data.numpy().squeeze()
    result = probability


if __name__ == '__main__':
    init()
    #test_on_single()
    app.run(host='0.0.0.0')
    app.debug = True  # enables auto reload during development
    app.run()

