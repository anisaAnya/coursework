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

  render() {
    console.log('AAAAAAAAAAAAAAAAAAAA', this.state);
    return (
      <div>
        <h1>Hello my friend!</h1>
      </div>
    );
  }
}
