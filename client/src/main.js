// bootstraping

import React from "react";

import {render} from "react-dom";

import {App} from "./app/App";
import Routes from "./app/Routes"
import {Provider} from "react-redux";
import store from "./app/store";


render((<Provider store={store}>
        <Routes />
        </Provider>), //virtual dom
        document.getElementById("root") //real dom
)