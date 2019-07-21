import React, { Component } from 'react';
import './app.css';


// const Express = require('express');
// const multer = require('multer');
// const bodyParser = require('body-parser');

// const app = Express();
// app.use(bodyParser.json());

/* let Storage = multer.diskStorage({
  destination(req, file, callback) {
    callback(null, './Images');
  },
  filename(req, file, callback) {
    callback(null, `${file.fieldname} ${Date.now()} ${file.originalname}`);
  }
}); */

export default class App extends Component {
  state = { username: null };

  componentDidMount() {
    fetch('http://192.168.0.107:8080').then(response => response.json()).then(data => this.setState({ username: data.username }));
    console.log('AAAAAAAAAAAAAAAAAAAA', this.state);
  }

  render() {
    console.log('AAAAAAAAAAAAAAAAAAAA', this.state);
    return (
      <div>
        <h1>Hello my friend!</h1>
        {/* eslint-disable-next-line max-len */}
        <text>I was created to help you distinguish between dogs and cats. Please use button below to upload an image and I will do the rest.</text>
        <form id="frmUploader" encType="multipart/form-data" action="api/Upload/" method="post">
          <input type="file" name="file" multiple />
          <input type="submit" name="submit" id="btnSubmit" value="Upload" />
        </form>
      </div>
    );
  }
}
