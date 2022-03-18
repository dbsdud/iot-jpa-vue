import Vue from 'vue';
import VueRouter from "vue-router";
import Home from './views/Home';
import About from "./views/About";
import Dashboard from "@/views/Dashboard";
import Devices from "@/views/Devices";

Vue.use(VueRouter);

const router = new VueRouter({
    mode: "history",
    routes: [
        {path: "/", component: Home},
        {path: "/about", component: About},
        {path: "/dashboard", component: Dashboard},
        {path: "/devices", component: Devices}
    ]
});

export default router;