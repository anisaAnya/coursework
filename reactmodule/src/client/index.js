import React from 'react';
import ReactDOM from 'react-dom';
// eslint-disable-next-line import/no-unresolved
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import App from './App';
import New from './New';

// ReactDOM.render(<App />, document.getElementById('root'));

// eslint-disable-next-line react/jsx-no-undef
ReactDOM.render(
  // eslint-disable-next-line react/jsx-no-undef
  <BrowserRouter>
    {/* eslint-disable-next-line react/jsx-no-undef */}
    <Switch>
      {/* eslint-disable-next-line react/jsx-no-undef */}
      <Route exact path="/" component={App} />
      <Route exact path="/api/Upload/" component={New} />
    </Switch>
  </BrowserRouter>, document.getElementById('root')
);
